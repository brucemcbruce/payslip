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
    public void getHello() throws Exception {
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

}
