package pl.tciesla.simulator.server.valuation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.simulator.server.dao.MutualFundDao;

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
    private MutualFundDao mutualFundDao;

    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void updateMutualFundsValuation() {
        mutualFundDao.fetchAll().forEach(mutualFund -> {
            logger.info("Fund[" + mutualFund.getId() + "] old valuation: " + mutualFund.getValuation());
            FundValuationStrategies.get(mutualFund).updateValuation(mutualFund);
            logger.info("Fund[" + mutualFund.getId() + "] new valuation: " + mutualFund.getValuation());
            mutualFundDao.persist(mutualFund);
        });
    }

}
