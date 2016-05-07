package pl.tciesla.simulator.server.valuation;

import pl.tciesla.simulator.commons.domain.MutualFund;
import pl.tciesla.simulator.server.dao.MutualFundDao;

import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.List;
import java.util.logging.Logger;

/**
 * Singleton generates new mutual funds valuation for every 10 seconds.
 */
@Singleton
@Startup
public class ValuationSourceBean {

    private static final Logger log = Logger.getLogger(ValuationSourceBean.class.getName());

    @EJB private MutualFundDao mutualFundDao;

    /**
     * Updates mutual funds valuation using strategy pattern.
     */
    @Schedule(hour = "*", minute = "*", second = "*/10")
    public void updateMutualFundsValuation() {
        List<MutualFund> mutualFunds = mutualFundDao.fetchAll();
        mutualFunds.forEach(fund -> FundValuationStrategies.getStrategy(fund.getCategory()).updateValuation(fund));
        mutualFunds.forEach(fund -> log.info("Fund[" + fund.getId() + "] new valuation: " + fund.getValuation()));
        mutualFundDao.persistAll(mutualFunds);
    }

    public void setMutualFundDao(MutualFundDao mutualFundDao) {
        this.mutualFundDao = mutualFundDao;
    }
}
