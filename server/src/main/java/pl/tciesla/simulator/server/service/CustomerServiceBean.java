package pl.tciesla.simulator.server.service;

import pl.tciesla.simulator.server.dao.CustomerDao;
import pl.tciesla.simulator.server.dao.MutualFundDao;
import pl.tciesla.simulator.server.domain.Customer;
import pl.tciesla.simulator.server.domain.MutualFund;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.function.BiFunction;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.*;

@Stateless
@Path("/customer")
public class CustomerServiceBean {

    private static final Logger log = Logger.getLogger(CustomerServiceBean.class.getName());

    private static final BigDecimal FEE_RATE = BigDecimal.valueOf(0.02);

    @EJB
    private CustomerDao customerDao;

    @EJB
    private MutualFundDao mutualFundDao;

    @GET
    @Path("/{username}")
    public Response getCustomer(@PathParam("username") String username) {
        log.info("received get request for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            log.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerDao.fetch(username);
        if (customer == null) {
            log.info("customer with username[" + username + "] not found");
            return Response.status(NOT_FOUND).build();
        }

        return Response.ok(customer).build();
    }

    @POST
    @Path("/{username}")
    public Response createCustomer(@PathParam("username") String username) {
        log.info("received post request for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            log.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }
        if (customerDao.fetch(username) != null) {
            log.info("given username[" + username + "] is already in use");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = Customer.builder()
                .username(username)
                .build();
        customerDao.persist(customer);
        log.info("customer with username[" + username + "] has been created");
        return Response.ok(customer).build();
    }

    @DELETE
    @Path("/{username}")
    public Response deleteCustomer(@PathParam("username") String username) {
        log.info("received delete request for customer[" + username + "]");

        if (username == null || username.isEmpty()) {
            log.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        boolean removed = customerDao.remove(username);
        if (!removed) {
            log.info("customer with username[" + username + "] not removed");
            return Response.status(INTERNAL_SERVER_ERROR).build();
        }

        log.info("customer with username[" + username + "] has been removed");
        return Response.ok().build();
    }

    @GET
    @Path("/{username}/buy/{fundId}/{amount}")
    public Response buyFundShares(
            @PathParam("username") String username,
            @PathParam("fundId") Long fundId,
            @PathParam("amount") Long amount) {

        log.info("received buy request for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            log.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerDao.fetch(username);
        if (customer == null) {
            log.info("customer with username[" + username + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (fundId == null) {
            log.info("incorrect mutual fund id");
            return Response.status(BAD_REQUEST).build();
        }

        MutualFund mutualFund = mutualFundDao.fetch(fundId);
        if (mutualFund == null) {
            log.info("fund with id[" + fundId + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (amount == null || amount <= 0) {
            log.info("incorrect shares amount");
            return Response.status(BAD_REQUEST).build();
        }

        BigDecimal transactionCost = calculateTransactionCashFlow(mutualFund, amount, BigDecimal::add);
        if (!customer.hasEnoughCash(transactionCost)) {
            log.info("customer do not have enough cash to buy shares.");
            return Response.status(BAD_REQUEST).build();
        }

        customer.buy(fundId, amount, transactionCost);
        customerDao.persist(customer);
        log.info("buy transaction completed");

        return Response.ok().build();
    }

    @GET
    @Path("/{username}/sell/{fundId}/{amount}")
    public Response sellShares(
            @PathParam("username") String username,
            @PathParam("fundId") Long fundId,
            @PathParam("amount") Long amount) {

        log.info("received sell request for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            log.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerDao.fetch(username);
        if (customer == null) {
            log.info("customer with username[" + username + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (fundId == null) {
            log.info("incorrect mutual fund id");
            return Response.status(BAD_REQUEST).build();
        }

        MutualFund mutualFund = mutualFundDao.fetch(fundId);
        if (mutualFund == null) {
            log.info("fund with id[" + fundId + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        if (amount == null || amount <= 0) {
            log.info("incorrect shares amount");
            return Response.status(BAD_REQUEST).build();
        }

        if (!customer.hasEnoughShares(fundId, amount)) {
            log.info("customer do not have enough shares to sell.");
            return Response.status(BAD_REQUEST).build();
        }

        BigDecimal transactionProfit = calculateTransactionCashFlow(mutualFund, amount, BigDecimal::subtract);
        customer.sell(fundId, amount, transactionProfit);
        customerDao.persist(customer);
        log.info("sell transaction completed.");

        return Response.ok().build();
    }

    @GET
    @Path("/{username}/valuation")
    public Response getWalletValuation(@PathParam("username") String username) {
        log.info("received wallet valuation request for username[" + username + "]");

        if (username == null || username.isEmpty()) {
            log.info("username cannot be empty");
            return Response.status(BAD_REQUEST).build();
        }

        Customer customer = customerDao.fetch(username);
        if (customer == null) {
            log.info("customer with username[" + username + "] not found");
            return Response.status(BAD_REQUEST).build();
        }

        BigDecimal walletValuation = customer.getFundShares().entrySet().stream()
                .map((entry) -> {
                    Long fundId = entry.getKey();
                    BigDecimal valuation = mutualFundDao.fetch(fundId).getValuation();
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
