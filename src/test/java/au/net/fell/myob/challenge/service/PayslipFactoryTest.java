package au.net.fell.myob.challenge.service;

import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.model.PayslipRequest;
import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PayslipFactoryTest {
    private PayslipFactory target = new PayslipFactory();

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
    public void populatesIncomeTaxBasedOnRequest() {
        PayslipRequest request = validRequest();
        request.setAnnualSalary(60050);

        Payslip result = target.generatePayslip(request);

        assertThat(result.getIncomeTax()).isEqualTo(922);
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