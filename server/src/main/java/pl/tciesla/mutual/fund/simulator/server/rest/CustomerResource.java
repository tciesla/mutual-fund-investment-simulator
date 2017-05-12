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

import static javax.ws.rs.core.Response.Status.*;

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

    @EJB
    private CustomerRepository customerRepository;

    @EJB
    private MutualFundRepository mutualFundRepository;

    @GET
    @Path(USERNAME_PARAM_PATH)
    public Response getCustomer(@PathParam(USERNAME_PARAM) String username) {
        Optional<Customer> customer = customerRepository.find(username);
        return customer.isPresent() ? Response.ok(customer.get()).build() :
                Response.status(NOT_FOUND).build();
    }

    @POST
    @Path(USERNAME_PARAM_PATH)
    public Response createCustomer(@PathParam(USERNAME_PARAM) String username) {
        if (customerRepository.find(username).isPresent()) {
            return Response.status(CONFLICT).build();
        }

        Customer customer = Customer.builder().username(username).build();
        customerRepository.save(customer);
        // TODO should return status CREATED(201)
        return Response.ok(customer).build();
    }

    @DELETE
    @Path(USERNAME_PARAM_PATH)
    public Response deleteCustomer(@PathParam(USERNAME_PARAM) String username) {
        if (!customerRepository.find(username).isPresent()) {
            return Response.status(NOT_FOUND).build();
        }

        customerRepository.delete(username);
        return Response.noContent().build();
    }

    @PUT
    @Path(USERNAME_PARAM_PATH + "/buy" + FUND_ID_PARAM_PATH + AMOUNT_PARAM_PATH)
    public Response buyFundShares(
            @PathParam(USERNAME_PARAM) String username,
            @PathParam(FUND_ID_PARAM) Long fundId,
            @PathParam(AMOUNT_PARAM) Long amount) {

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }

        Optional<MutualFund> mutualFundOptional = mutualFundRepository.find(fundId);
        if (!mutualFundOptional.isPresent()) {
            return Response.status(NOT_FOUND).build();
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

    @PUT
    @Path(USERNAME_PARAM_PATH + "/sell" + FUND_ID_PARAM_PATH + AMOUNT_PARAM_PATH)
    public Response sellShares(
            @PathParam(USERNAME_PARAM) String username,
            @PathParam(FUND_ID_PARAM) Long fundId,
            @PathParam(AMOUNT_PARAM) Long amount) {

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            return Response.status(NOT_FOUND).build();
        }

        Optional<MutualFund> mutualFundOptional = mutualFundRepository.find(fundId);
        if (!mutualFundOptional.isPresent()) {
            return Response.status(NOT_FOUND).build();
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

    private BigDecimal calculateTransactionCashFlow(MutualFund mutualFund, Long amount,
            BiFunction<BigDecimal, BigDecimal, BigDecimal> f) {

        BigDecimal currentFundValuation = mutualFund.getValuation();
        BigDecimal transactionValue = currentFundValuation.multiply(BigDecimal.valueOf(amount));
        BigDecimal fee = transactionValue.multiply(FEE_RATE).setScale(2, BigDecimal.ROUND_UP);
        return f.apply(transactionValue, fee);
    }

}
