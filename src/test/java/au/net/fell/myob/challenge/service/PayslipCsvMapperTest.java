package au.net.fell.myob.challenge.service;

import au.net.fell.myob.challenge.model.Payslip;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PayslipCsvMapperTest {

    @InjectMocks
    private PayslipCsvMapper target;

    @Mock
    private PayslipFactory payslipFactory;

    @Spy
    private CsvMapper csvMapper;

    @Before
    public void setUp() throws Exception {
        csvMapper.registerModule(new JavaTimeModule());
    }

    @Test(expected = IllegalArgumentException.class)
    public void onlyAttemptsToMapNonBlankStrings() {
        target.generatePayslipCsv(" ");

        // exception should be thrown
    }

    @Test
    public void correctlyMapsPayslipsToCsv() {
        String requestCsv = "firstName,lastName,annualIncome,superannuationRate,paymentDate\n" +
                "David,Rudd,60050,9,2017-03-01\n" +
                "Ryan,Chen,120000,10,2017-03-01\n";
        when(payslipFactory.generatePayslip(any())).thenReturn(expectedPayslip1(), expectedPayslip2());

        String result = target.generatePayslipCsv(requestCsv);

        assertThat(result).isEqualTo("name,payPeriodStart,payPeriodEnd,grossIncome,incomeTax,netIncome,superannuation\n" +
                "\"David Rudd\",2017-03-01,2017-03-31,5004,922,4082,450\n" +
                "\"Ryan Chen\",2017-03-01,2017-03-31,10000,2696,7304,1000\n");
    }

    private Payslip expectedPayslip1() {
        Payslip payslip = new Payslip();
        payslip.setIncomeTax(922);
        payslip.setSuperannuation(450);
        payslip.setGrossIncome(5004);
        payslip.setNetIncome(4082);
        payslip.setName("David Rudd");
        payslip.setPayPeriodStart(LocalDate.of(2017, 3, 1));
        payslip.setPayPeriodEnd(LocalDate.of(2017, 3, 31));
        return payslip;
    }

    private Payslip expectedPayslip2() {
        Payslip payslip = new Payslip();
        payslip.setIncomeTax(2696);
        payslip.setSuperannuation(1000);
        payslip.setGrossIncome(10000);
        payslip.setNetIncome(7304);
        payslip.setName("Ryan Chen");
        payslip.setPayPeriodStart(LocalDate.of(2017, 3, 1));
        payslip.setPayPeriodEnd(LocalDate.of(2017, 3, 31));
        return payslip;
    }

}