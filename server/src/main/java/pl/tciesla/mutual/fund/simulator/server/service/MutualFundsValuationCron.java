package pl.tciesla.mutual.fund.simulator.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.mutual.fund.simulator.server.repository.MutualFundRepository;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Cron generates new mutual funds valuation for every 10 seconds.
 */
@Singleton
@Startup
public class MutualFundsValuationCron {

    private static final Logger logger = LoggerFactory.getLogger(MutualFundsValuationCron.class);

    @EJB
    private MutualFundRepository mutualFundRepository;

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void updateMutualFundsValuation() {
        mutualFundRepository.findAll().forEach(mutualFund -> {
            logger.info("Fund[" + mutualFund.getId() + "] old valuation: " + mutualFund.getValuation());
            FundValuationStrategies.getStrategy(mutualFund.getCategory()).updateValuation(mutualFund);
            logger.info("Fund[" + mutualFund.getId() + "] new valuation: " + mutualFund.getValuation());
            mutualFundRepository.save(mutualFund);
        });
    }

}
