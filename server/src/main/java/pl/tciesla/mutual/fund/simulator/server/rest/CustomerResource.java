package pl.tciesla.mutual.fund.simulator.server.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.mutual.fund.simulator.server.domain.Customer;
import pl.tciesla.mutual.fund.simulator.server.domain.MutualFund;
import pl.tciesla.mutual.fund.simulator.server.repository.CustomerRepository;
import pl.tciesla.mutual.fund.simulator.server.repository.MutualFundRepository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.BiFunction;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Stateless
@Path("/customers")
@Produces(MediaType.APPLICATION_XML)
public class CustomerResource {

    private static final Logger logger = LoggerFactory.getLogger(CustomerResource.class);

    private static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.02);

    @EJB
    private CustomerRepository customerRepository;

    @EJB
    private MutualFundRepository mutualFundRepository;

    @GET
    @Path("/{username}")
    public Response getCustomer(@PathParam("username") String username) {
        logger.info("request get customer for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            logger.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customer = customerRepository.find(username);
        if (!customer.isPresent()) {
            logger.info("customer with username[" + username + "] not found");
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(customer.get()).build();
    }

    @POST
    @Path("/{username}")
    public Response createCustomer(@PathParam("username") String username) {
        logger.info("request create customer for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            logger.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }
        if (customerRepository.find(username).isPresent()) {
            logger.info("given username[" + username + "] is already in use");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = Customer.builder().username(username).build();
        logger.info("customer with username[" + username + "] has been created");
        customerRepository.save(customer);
        return Response.ok(customer).build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteCustomer(@PathParam("username") String username) {
        logger.info("request delete customer for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            logger.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        customerRepository.delete(username);
        logger.info("customer with username[" + username + "] has been removed");
        return Response.ok().build();
    }

    @GET
    @Path("/{username}/buy/{fundId}/{amount}")
    public Response buyFundShares(
            @PathParam("username") String username,
            @PathParam("fundId") Long fundId,
            @PathParam("amount") Long amount) {

        logger.info("request buy shares for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            logger.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            logger.info("customer with username[" + username + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (fundId == null) {
            logger.info("incorrect mutual fund id");
            return Response.status(BAD_REQUEST).build();
        }

        Optional<MutualFund> mutualFundOptional = mutualFundRepository.find(fundId);
        if (!mutualFundOptional.isPresent()) {
            logger.info("fund with id[" + fundId + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (amount == null || amount <= 0) {
            logger.info("incorrect shares amount");
            return Response.status(BAD_REQUEST).build();
        }

        MutualFund mutualFund = mutualFundOptional.get();
        BigDecimal transactionCost = calculateTransactionCashFlow(mutualFund, amount, BigDecimal::add);
        Customer customer = customerOptional.get();
        if (!customer.hasEnoughCash(transactionCost)) {
            logger.info("customer do not have enough cash to buy shares.");
            return Response.status(BAD_REQUEST).build();
        }

        customer.buy(fundId, amount, transactionCost);
        customerRepository.save(customer);
        logger.info("buy transaction completed");
        return Response.ok().build();
    }

    @GET
    @Path("/{username}/sell/{fundId}/{amount}")
    public Response sellShares(
            @PathParam("username") String username,
            @PathParam("fundId") Long fundId,
            @PathParam("amount") Long amount) {

        logger.info("request sell shares for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            logger.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            logger.info("customer with username[" + username + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (fundId == null) {
            logger.info("incorrect mutual fund id");
            return Response.status(BAD_REQUEST).build();
        }

        Optional<MutualFund> mutualFundOptional = mutualFundRepository.find(fundId);
        if (!mutualFundOptional.isPresent()) {
            logger.info("fund with id[" + fundId + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (amount == null || amount <= 0) {
            logger.info("incorrect shares amount");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerOptional.get();
        if (!customer.hasEnoughShares(fundId, amount)) {
            logger.info("customer do not have enough shares to sell.");
            return Response.status(BAD_REQUEST).build();
        }

        MutualFund mutualFund = mutualFundOptional.get();
        BigDecimal transactionProfit = calculateTransactionCashFlow(mutualFund, amount, BigDecimal::subtract);
        customer.sell(fundId, amount, transactionProfit);
        customerRepository.save(customer);
        logger.info("sell transaction completed.");
        return Response.ok().build();
    }

    @GET
    @Path("/{username}/valuation")
    public Response getWalletValuation(@PathParam("username") String username) {
        logger.info("request wallet valuation for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            logger.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Optional<Customer> customerOptional = customerRepository.find(username);
        if (!customerOptional.isPresent()) {
            logger.info("customer with username[" + username + "] not found");
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
