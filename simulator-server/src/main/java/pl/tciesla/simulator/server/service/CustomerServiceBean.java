package pl.tciesla.simulator.server.service;

import pl.tciesla.simulator.server.builder.CustomerBuilder;
import pl.tciesla.simulator.server.builder.FundSharesBuilder;
import pl.tciesla.simulator.server.constant.ShareType;
import pl.tciesla.simulator.server.dao.CustomerDao;
import pl.tciesla.simulator.server.dao.MutualFundDao;
import pl.tciesla.simulator.server.domain.Customer;
import pl.tciesla.simulator.server.domain.FundShares;
import pl.tciesla.simulator.server.domain.MutualFund;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

/**
 * REST web services to access customer resources.
 */
@Path("/customer")
@Stateless
public class CustomerServiceBean {

    private static final Logger log = Logger.getLogger(CustomerServiceBean.class.getName());

    private static final String FEE_RATE = "0.02";

    @EJB private CustomerDao customerDao;
    @EJB private MutualFundDao mutualFundDao;

    @GET
    @Path("/{username}")
    public Response getCustomer(@PathParam("username") String username) {
        log.info("received get request for customer[" + username + "]");
        createCustomerIfNotExists(username);
        return Response.ok(customerDao.fetch(username)).build();
    }

    private void createCustomerIfNotExists(String username) {
        if (customerDao.fetch(username) == null) {
            customerDao.persist(CustomerBuilder.aCustomerBuilder()
                    .withName(username)
                    .build());
            log.info("customer[" + username + "] has been created");
        }
    }

    @DELETE
    @Path("/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteCustomer(@PathParam("username") String username) {
        log.info("received delete request for customer[" + username + "]");
        customerDao.remove(username);
        log.info("customer[" + username + "] has been removed");
        return "Delete operation completed.";
    }

    @GET
    @Path("/{username}/buy/{fundId}/{type}/{amount}")
    @Produces(MediaType.TEXT_PLAIN)
    public String buyShares(@PathParam("username") String username,
                              @PathParam("fundId") long fundId,
                              @PathParam("type") ShareType shareType,
                              @PathParam("amount") long amount) {
        if (amount <= 0) { return "Shares amount have to be greater than zero"; }
        if (fundId <= 0) { return "Fund id have to be greater than zero."; }
        MutualFund mutualFund = mutualFundDao.fetch(fundId);
        if (mutualFund == null) { return "Fund with given id does not exists."; }
        BigDecimal transactionValue = mutualFund.getValuation().multiply(new BigDecimal(amount));
        BigDecimal fee = calculateTransactionFee(transactionValue);
        BigDecimal transactionCost = transactionValue.add(fee);

        createCustomerIfNotExists(username);
        Customer customer = customerDao.fetch(username);
        if (hasEnoughCash(transactionCost, customer)) {
            customer.setCash(customer.getCash().subtract(transactionCost));
            FundShares fundShares = createFundShares(fundId, shareType);
            if (customer.getFundShares().containsKey(fundShares)) {
                long previousSharesAmount = customer.getFundShares().get(fundShares);
                customer.getFundShares().put(fundShares, previousSharesAmount + amount);
            } else {
                customer.getFundShares().put(fundShares, amount);
            }
            customerDao.persist(customer);
            return "Transaction completed.";
        } else {
            return "You do not have enough cash to buy shares.";
        }
    }

    private BigDecimal calculateTransactionFee(BigDecimal transactionValue) {
        BigDecimal fee = transactionValue.multiply(new BigDecimal(FEE_RATE));
        return fee.setScale(2, BigDecimal.ROUND_UP);
    }

    private boolean hasEnoughCash(BigDecimal totalCost, Customer customer) {
        int compareResult = customer.getCash().compareTo(totalCost);
        return compareResult == 0 || compareResult == 1;
    }

    private FundShares createFundShares(long fundId, ShareType shareType) {
        return FundSharesBuilder.aFundSharesBuilder()
                .withFundId(fundId)
                .withType(shareType)
                .build();
    }

    @GET
    @Path("/{username}/sell/{fundId}/{type}/{amount}")
    @Produces(MediaType.TEXT_PLAIN)
    public String sellShares(@PathParam("username") String username,
                               @PathParam("fundId") long fundId,
                               @PathParam("type") ShareType shareType,
                               @PathParam("amount") long amount) {
        if (amount <= 0) { return "Shares amount have to be greater than zero"; }
        if (fundId <= 0) { return "Fund id have to be greater than zero."; }
        MutualFund mutualFund = mutualFundDao.fetch(fundId);
        if (mutualFund == null) { return "Fund with given id does not exists."; }
        createCustomerIfNotExists(username);
        Customer customer = customerDao.fetch(username);
        FundShares fundShares = createFundShares(fundId, shareType);
        if (hasEnoughShares(fundShares, amount, customer)) {
            long previousSharesAmount = customer.getFundShares().get(fundShares);
            customer.getFundShares().put(fundShares, previousSharesAmount - amount);
            if (customer.getFundShares().get(fundShares) == 0) {
                customer.getFundShares().remove(fundShares);
            }
            BigDecimal transactionValue = mutualFund.getValuation().multiply(new BigDecimal(amount));
            BigDecimal transactionRevenue = transactionValue.subtract(calculateTransactionFee(transactionValue));
            customer.setCash(customer.getCash().add(transactionRevenue));
            customerDao.persist(customer);
            return "Transaction completed.";
        } else {
            return "You do not have enough shares to sell.";
        }
    }

    private boolean hasEnoughShares(FundShares fundShares, long amount, Customer customer) {
        return customer.getFundShares().getOrDefault(fundShares, 0L) >= amount;
    }

    @GET
    @Path("/{username}/valuation")
    @Produces(MediaType.TEXT_PLAIN)
    public String getWalletValuation(@PathParam("username") String username) {
        createCustomerIfNotExists(username);
        Customer customer = customerDao.fetch(username);
        BigDecimal walletValuation = BigDecimal.ZERO;
        for (Map.Entry<FundShares, Long> entry : customer.getFundShares().entrySet()) {
            long fundId = entry.getKey().getFundId();
            BigDecimal fundValuation = mutualFundDao.fetch(fundId).getValuation();
            BigDecimal sharesAmount = new BigDecimal(entry.getValue());
            walletValuation = walletValuation.add(fundValuation.multiply(sharesAmount));
        }
        walletValuation = walletValuation.add(customer.getCash());
        return walletValuation.toString();
    }
}
