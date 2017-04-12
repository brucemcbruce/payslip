package au.net.fell.myob.challenge.service;

import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.model.PayslipRequest;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(DataProviderRunner.class)
public class PayslipFactoryTest {

    private PayslipFactory target = new PayslipFactory(new IncomeTaxCalculator());

    @Test
    public void generatesPayslipFromRequest() {
        PayslipRequest request = validRequest();

        Payslip result = target.generatePayslip(request);

        assertThat(result).isNotNull();
    }

    @Test
    public void populatesNameBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setFirstName("John");
        request.setLastName("Smith");

        Payslip result = target.generatePayslip(request);

        assertThat(result.getName()).isEqualTo("John Smith");
    }

    @Test
    public void populatesStartAndEndOfPeriodBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setPaymentDate(LocalDate.of(2017, 4, 12));

        Payslip result = target.generatePayslip(request);

        assertThat(result.getPayPeriodStart()).isEqualTo(LocalDate.of(2017, 4, 1));
        assertThat(result.getPayPeriodEnd()).isEqualTo(LocalDate.of(2017, 4, 30));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsErrorGivenNoPaymentDate() {
        PayslipRequest request = validRequest();
        request.setPaymentDate(null);

        Payslip result = target.generatePayslip(request);

        // exception should be thrown
    }

    @Test
    public void populatesGrossIncomeBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(60050);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getGrossIncome()).isEqualTo(5004);
    }

    @Test
    @UseDataProvider("roundingDataProvider")
    public void correctlyHandlesRounding(int annualSalary, int expectedGrossIncome, String testCase) {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(annualSalary);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getGrossIncome()).as(testCase).isEqualTo(expectedGrossIncome);
    }

    @DataProvider
    public static Object[][] roundingDataProvider() {
        return new Object[][] {
                {60050, 5004, "Provided sample case (rounding down)"},
                {60060, 5005, "Exact value"},
                {60070, 5006, "Value to be rounded up"},
                {60078, 5007, "Half-way value (rounding up)"}
        };
    }

    @Test
    @UseDataProvider("incomeTaxDataProvider")
    public void populatesIncomeTaxBasedOnRequest(int annualSalary, int expectedIncomeTax, String testCase) {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(60050);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getIncomeTax()).isEqualTo(922);
    }

    @DataProvider
    public static Object[][] incomeTaxDataProvider() {
        return new Object[][]{
                {60050, 922, "Provided sample case 1"},
                {120000, 2696, "Provided sample case 2"},
                {18200, 0, "Tax free threshold"},
                {18232, 1, "Smallest income that pays any tax"},
                {37000, 298, "Top of tax bracket 1"},
                {80000, 1462, "Top of tax bracket 2"},
                {180000, 4546, "Top of tax bracket 3"},
                {250000, 7171, "High income earner"},
        };
    }

    @Test
    public void populatesNetIncomeBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(60050);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getNetIncome()).isEqualTo(4082);
    }

    @Test
    public void populatesSuperannuationBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(60050);
        request.setSuperRate(9);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getSuperannuation()).isEqualTo(450);
    }

    /**
     * @return a minimally-valid request that will pass all standard validations. This should be customised for each test's needs.
     */
    private PayslipRequest validRequest() {
        PayslipRequest request = new PayslipRequest();
        request.setPaymentDate(LocalDate.now());
        request.setAnnualSalary(120000);
        return request;
    }

}