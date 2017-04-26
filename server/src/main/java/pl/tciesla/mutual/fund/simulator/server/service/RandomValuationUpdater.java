package pl.tciesla.mutual.fund.simulator.server.service;

import com.google.common.base.Preconditions;
import pl.tciesla.mutual.fund.simulator.server.model.MutualFund;

import java.math.BigDecimal;
import java.util.Random;

import static java.lang.Math.abs;

public class RandomValuationUpdater {

    private static final Random random = new Random(System.currentTimeMillis());

    /**
     * Updates mutual fund valuation with random change in cents from range.
     * @param centsFrom should be value less or equal to zero
     * @param centsTo should be value greater or equal to zero
     */
    public static void updateFromRange(MutualFund mutualFund, int centsFrom, int centsTo) {
        Preconditions.checkNotNull(mutualFund, "mutualFund == null");
        Preconditions.checkArgument(centsFrom <= 0, "centsFrom > 0");
        Preconditions.checkArgument(centsTo >= 0, "centsTo < 0");

        BigDecimal change = generateChangeBetween(centsFrom, centsTo);
        BigDecimal previousValuation = mutualFund.getValuation();
        BigDecimal valuation = previousValuation.add(change);
        if (BigDecimal.ZERO.compareTo(valuation) == 1) return;
        mutualFund.updateValuation(valuation);
    }

    /**
     * Generates random change in cents.
     * @return change in format X.XX
     */
    private static BigDecimal generateChangeBetween(int centsFrom, int centsTo) {
        int cents = random.nextInt(abs(centsFrom) + centsTo + 1) + centsFrom;
        BigDecimal change = new BigDecimal(cents / 100.0);
        return change.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

}
