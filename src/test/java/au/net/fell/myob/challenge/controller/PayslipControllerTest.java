package au.net.fell.myob.challenge.controller;

import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.model.PayslipRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PayslipControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void putSinglePayslip() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/payslip").
                accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).
                content(toJson(validPayslipRequest()))
        ).andExpect(status().isOk()).
                andExpect(content().string(equalTo(toJson(expectedResponse()))));
    }

    private String toJson(Object model) {
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            fail("Error mapping JSON: " + e.getMessage());
            throw new RuntimeException("This should never be reached");
        }
    }

    private Object validPayslipRequest() {
        PayslipRequest request = new PayslipRequest();
        request.setFirstName("David");
        request.setLastName("Rudd");
        request.setAnnualSalary(60050);
        request.setSuperRate(9);
        request.setPaymentDate(LocalDate.of(2017, 3,1));
        return request;
    }

    private Payslip expectedResponse() {
        Payslip response = new Payslip();
        response.setName("David Rudd");
        response.setPayPeriodStart(LocalDate.of(2017,3,1));
        response.setPayPeriodEnd(LocalDate.of(2017,3,31));
        response.setGrossIncome(5004);
        response.setIncomeTax(922);
        response.setNetIncome(4082);
        response.setSuperannuation(450);
        return response;
    }

    @Test
    public void putCsv() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/payslipCsv").
                accept("text/csv").contentType("text/csv").
                content(validCsvRequest())
        ).andExpect(status().isOk()).
                andExpect(content().string(equalTo(expectedCsvResponse())));
    }

    private String validCsvRequest() {
        return "firstName,lastName,annualIncome,superannuationRate,paymentDate\n" +
                "David,Rudd,60050,9,2017-03-01\n" +
                "Ryan,Chen,120000,10,2017-03-01\n";
    }

    private String expectedCsvResponse() {
        return "name,payPeriodStart,payPeriodEnd,grossIncome,incomeTax,netIncome,superannuation\n" +
                "\"David Rudd\",2017-03-01,2017-03-31,5004,922,4082,450\n" +
                "\"Ryan Chen\",2017-03-01,2017-03-31,10000,2696,7304,1000\n";
    }

}
