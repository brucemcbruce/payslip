package au.net.fell.myob.challenge.service;

import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.model.PayslipRequest;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(DataProviderRunner.class)
public class PayslipFactoryTest {

    @InjectMocks
    private PayslipFactory target;

    @Mock
    private IncomeTaxCalculator incomeTaxCalculator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(incomeTaxCalculator.calculate(anyInt())).thenReturn(123);
    }

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
    @UseDataProvider("grossPayDataProvider")
    public void correctlyHandlesRounding(int annualSalary, int expectedGrossIncome, String testCase) {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(annualSalary);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getGrossIncome()).as(testCase).isEqualTo(expectedGrossIncome);
    }

    @DataProvider
    public static Object[][] grossPayDataProvider() {
        return new Object[][]{
                {60050, 5004, "Provided sample case (rounding down)"},
                {60060, 5005, "Exact value"},
                {60070, 5006, "Value to be rounded up"},
                {60078, 5007, "Half-way value (rounding up)"}
        };
    }

    @Test
    @UseDataProvider("incomeTaxDataProvider")
    public void populatesIncomeTaxBasedOnRequest(int calculatedTax, int expectedIncomeTax, String testCase) {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(123456);
        when(incomeTaxCalculator.calculate(123456)).thenReturn(calculatedTax);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getIncomeTax()).isEqualTo(expectedIncomeTax);
    }

    @DataProvider
    public static Object[][] incomeTaxDataProvider() {
        return new Object[][]{
                {11063, 922, "Provided sample case"},
                {1200, 100, "No rounding"},
                {1215, 101, "Round down"},
                {1221, 102, "Round up"},
                {1230, 103, "Exactly half-way (round up)"},
        };
    }

    @Test
    public void populatesNetIncomeBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(60050);
        when(incomeTaxCalculator.calculate(60050)).thenReturn(11063);

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