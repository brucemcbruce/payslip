package au.net.fell.myob.challenge.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@Service
public class IncomeTaxCalculator {

    private Set<TaxBracket> taxBrackets;

    public IncomeTaxCalculator() {
        taxBrackets = new HashSet<>();
        taxBrackets.add(new TaxBracket(0, 18200, 0, 0));
        taxBrackets.add(new TaxBracket(18201, 37000, 0, 0.19f));
        taxBrackets.add(new TaxBracket(37001, 80000, 3572, 0.325f));
        taxBrackets.add(new TaxBracket(80001, 180000, 17547, 0.37f));
        taxBrackets.add(new TaxBracket(180001, Integer.MAX_VALUE, 54547, 0.45f));
    }

    public int calculate(int income) {
        return Math.round(taxBrackets.stream().
                filter(it -> income >= it.minimumIncome && income <= it.maximumIncome).
                findAny().orElseThrow(() -> new IllegalArgumentException("No matching tax bracket found for income " + income + ", check configuration!")).
                calculate(income));
    }

    private class TaxBracket {
        final int minimumIncome;
        final int maximumIncome;
        final int baseTaxAmount;
        final float centsPerAdditionalDollar;

        public TaxBracket(int minimumIncome, int maximumIncome, int baseTaxAmount, float centsPerAdditionalDollar) {
            this.minimumIncome = minimumIncome;
            this.maximumIncome = maximumIncome;
            this.baseTaxAmount = baseTaxAmount;
            this.centsPerAdditionalDollar = centsPerAdditionalDollar;
        }

        public float calculate(int income) {
            checkArgument(income >= minimumIncome && income <= maximumIncome);
            return baseTaxAmount + centsPerAdditionalDollar * (income - minimumIncome);
        }
    }
}
