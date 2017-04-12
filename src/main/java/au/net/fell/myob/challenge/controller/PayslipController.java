package au.net.fell.myob.challenge.controller;

import au.net.fell.myob.challenge.model.PayslipRequest;
import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.service.PayslipFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class PayslipController {

    @Autowired
    private PayslipFactory payslipFactory;

    // endpoint uses the PUT method, as it is an idempotent operation
    @RequestMapping(value = "/payslip", method = PUT)
    @ResponseBody
    public Payslip generatePayslip(@RequestBody PayslipRequest request) throws Exception {
        return payslipFactory.generatePayslip(request);
    }
}
