package pl.tciesla.simulator.server.valuation.util;

import pl.tciesla.simulator.commons.domain.MutualFund;

import java.math.BigDecimal;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * Util class that implement random update mutual funds valuation.
 */
public class RandomValuationUpdater {

    private static final Random random = new Random();

    public static void updateValuation(MutualFund mutualFund, int penniesFrom, int penniesTo) {
        BigDecimal change = generateChangeBetween(penniesFrom, penniesTo);
        BigDecimal previousValuation = mutualFund.getValuation();
        BigDecimal actualValuation = previousValuation.add(change);
        if (BigDecimal.ZERO.compareTo(actualValuation) == 1) return;
        mutualFund.setValuation(previousValuation.add(change));
    }

    /**
     * Generate random change in pennies.
     * @param penniesFrom should be value less or equal to zero.
     * @param penniesTo should be value greater or equal to zero.
     * @return change in format X.XX
     */
    private static BigDecimal generateChangeBetween(int penniesFrom, int penniesTo) {
        int pennies = random.nextInt(abs(penniesFrom) + penniesTo + 1) + penniesFrom;
        BigDecimal change = new BigDecimal(pennies / 100.0);
        return change.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
