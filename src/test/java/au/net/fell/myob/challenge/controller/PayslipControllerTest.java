package au.net.fell.myob.challenge.controller;

import au.net.fell.myob.challenge.model.PayslipRequestModel;
import au.net.fell.myob.challenge.model.PayslipResponseModel;
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
                content(toJson(new PayslipRequestModel()))
        ).andExpect(status().isOk()).
                andExpect(content().string(equalTo(expectedResponse())));
    }

    private String toJson(Object model) {
        try {
            return objectMapper.writeValueAsString(model);
        } catch (JsonProcessingException e) {
            fail("Error mapping JSON: " + e.getMessage());
            throw new RuntimeException("This should never be reached");
        }
    }

    private String expectedResponse() {
        return toJson(new PayslipResponseModel());
    }
}
