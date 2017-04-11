package au.net.fell.myob.challenge.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PayslipController {

    @RequestMapping(value = "/generate")
    @ResponseBody
    public String generatePayslip() {
        return "Here is your payslip";
    }
}
