package pl.tciesla.mutual.fund.simulator.server.rest;

import pl.tciesla.mutual.fund.simulator.server.model.Customer;
import pl.tciesla.mutual.fund.simulator.server.model.MutualFund;
import pl.tciesla.mutual.fund.simulator.server.repository.CustomerRepository;
import pl.tciesla.mutual.fund.simulator.server.repository.MutualFundRepository;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiFunction;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/customers")
@Produces(MediaType.APPLICATION_XML)
public class CustomerResource {

    private static final String USERNAME_PARAM = "username";
    private static final String USERNAME_PARAM_PATH = "/{username: [a-z]+[0-9]*}";
    private static final String FUND_ID_PARAM = "fundId";
    private static final String FUND_ID_PARAM_PATH = "/{fundId: [1-9][0-9]*}";
    private static final String AMOUNT_PARAM = "amount";
    private static final String AMOUNT_PARAM_PATH = "/{amount: [1-9][0-9]*}";

    private static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.02);

    @EJB private CustomerRepository customerRepository;
    @EJB private MutualFundRepository mutualFundRepository;

    @GET
    @Path(USERNAME_PARAM_PATH)
    public Response getCustomer(@PathParam(USERNAME_PARAM) String username) {

        if (username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customer = customerRepository.find(username);
        if (!customer.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(customer.get()).build();
    }

    @POST
    @Path(USERNAME_PARAM_PATH)
    public Response createCustomer(@PathParam(USERNAME_PARAM) String username) {

        if (username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }
        if (customerRepository.find(username).isPresent()) {
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = Customer.builder().username(username).build();
        customerRepository.save(customer);
        return Response.ok(customer).build();
    }

    @DELETE
    @Path(USERNAME_PARAM_PATH)
    public Response deleteCustomer(@PathParam(USERNAME_PARAM) String username) {

        if (username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }

        customerRepository.delete(username);
        return Response.ok().build();
    }

    @GET
    @Path(USERNAME_PARAM_PATH + "/buy" + FUND_ID_PARAM_PATH + AMOUNT_PARAM_PATH)
    public Response buyFundShares(
            @PathParam(USERNAME_PARAM) String username,
            @PathParam(FUND_ID_PARAM) Long fundId,
            @PathParam(AMOUNT_PARAM) Long amount) {

        if (username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            return Response.status(BAD_REQUEST).build();
        }

        if (fundId == null) {
            return Response.status(BAD_REQUEST).build();
        }

        Optional<MutualFund> mutualFundOptional = mutualFundRepository.find(fundId);
        if (!mutualFundOptional.isPresent()) {
            return Response.status(BAD_REQUEST).build();
        }

        if (amount == null || amount <= 0) {
            return Response.status(BAD_REQUEST).build();
        }

        MutualFund mutualFund = mutualFundOptional.get();
        BigDecimal transactionCost = calculateTransactionCashFlow(mutualFund, amount, BigDecimal::add);
        Customer customer = customerOptional.get();
        if (!customer.hasEnoughCash(transactionCost)) {
            return Response.status(BAD_REQUEST).build();
        }

        customer.buy(fundId, amount, transactionCost);
        customerRepository.save(customer);
        return Response.ok().build();
    }

    @GET
    @Path(USERNAME_PARAM_PATH + "/sell" + FUND_ID_PARAM_PATH + AMOUNT_PARAM_PATH)
    public Response sellShares(
            @PathParam(USERNAME_PARAM) String username,
            @PathParam(FUND_ID_PARAM) Long fundId,
            @PathParam(AMOUNT_PARAM) Long amount) {

        if (username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            return Response.status(BAD_REQUEST).build();
        }

        if (fundId == null) {
            return Response.status(BAD_REQUEST).build();
        }

        Optional<MutualFund> mutualFundOptional = mutualFundRepository.find(fundId);
        if (!mutualFundOptional.isPresent()) {
            return Response.status(BAD_REQUEST).build();
        }

        if (amount == null || amount <= 0) {
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerOptional.get();
        if (!customer.hasEnoughShares(fundId, amount)) {
            return Response.status(BAD_REQUEST).build();
        }

        MutualFund mutualFund = mutualFundOptional.get();
        BigDecimal transactionProfit = calculateTransactionCashFlow(mutualFund, amount, BigDecimal::subtract);
        customer.sell(fundId, amount, transactionProfit);
        customerRepository.save(customer);

        return Response.ok().build();
    }

    @GET
    @Path(USERNAME_PARAM_PATH + "/valuation")
    public Response getWalletValuation(@PathParam(USERNAME_PARAM) String username) {

        if (username == null || username.isEmpty()) {
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerOptional.get();
        BigDecimal walletValuation = customer.getFundShares().entrySet().stream()
                .map((entry) -> {
                    Long fundId = entry.getKey();
                    BigDecimal valuation = mutualFundRepository.find(fundId).get().getValuation();
                    Long shares = entry.getValue();
                    return valuation.multiply(BigDecimal.valueOf(shares));
                }).reduce(customer.getCash(), BigDecimal::add);

        return Response.ok(walletValuation).build();
    }

    private BigDecimal calculateTransactionCashFlow(MutualFund mutualFund, Long amount,
                                                    BiFunction<BigDecimal, BigDecimal, BigDecimal> f) {

        BigDecimal currentFundValuation = mutualFund.getValuation();
        BigDecimal transactionValue = currentFundValuation.multiply(BigDecimal.valueOf(amount));
        BigDecimal fee = transactionValue.multiply(FEE_RATE).setScale(2, BigDecimal.ROUND_UP);
        return f.apply(transactionValue, fee);
    }

}
