package au.net.fell.myob.challenge.controller;

import au.net.fell.myob.challenge.model.PayslipRequestModel;
import au.net.fell.myob.challenge.model.PayslipResponseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class PayslipController {

    @Autowired
    private ObjectMapper objectMapper;

    // endpoint uses a PUT method, as it is an idempotent operation
    @RequestMapping(value = "/payslip", method = PUT)
    @ResponseBody
    public PayslipResponseModel generatePayslip(@RequestBody PayslipRequestModel request) throws Exception {
        return new PayslipResponseModel();
    }
}
