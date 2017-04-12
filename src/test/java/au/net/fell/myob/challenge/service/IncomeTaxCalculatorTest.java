package au.net.fell.myob.challenge.service;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class IncomeTaxCalculatorTest {

    IncomeTaxCalculator target = new IncomeTaxCalculator();

    @Test
    @UseDataProvider("incomeTaxDataProvider")
    public void populatesIncomeTaxBasedOnRequest(int income, int expectedIncomeTax, String testCase) {
        int result = target.calculate(income);

        assertThat(result).isEqualTo(expectedIncomeTax);
    }

    @DataProvider
    public static Object[][] incomeTaxDataProvider() {
        return new Object[][]{
                {60050, 11063, "Provided sample case 1"},
                {120000, 32347, "Provided sample case 2"},
                {18200, 0, "Tax free threshold"},
                {18232, 6, "Smallest income that pays any tax"},
                {37000, 3572, "Top of tax bracket 1"},
                {80000, 17547, "Top of tax bracket 2"},
                {180000, 54547, "Top of tax bracket 3"},
                {250000, 86047, "High income earner"},
        };
    }


}