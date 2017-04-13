package au.net.fell.myob.challenge.controller;

import au.net.fell.myob.challenge.model.PayslipRequest;
import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.service.PayslipCsvMapper;
import au.net.fell.myob.challenge.service.PayslipFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

// endpoints use the PUT method, as they are idempotent operations
@Controller
public class PayslipController {

    @Autowired
    private PayslipFactory payslipFactory;
    @Autowired
    private PayslipCsvMapper payslipCsvMapper;

    @RequestMapping(value = "/payslip", method = PUT)
    @ResponseBody
    public Payslip generatePayslip(@RequestBody PayslipRequest request) throws Exception {
        return payslipFactory.generatePayslip(request);
    }

    @RequestMapping(value = "/payslipCsv", method = PUT, consumes = {"text/csv"}, produces = {"text/csv"})
    @ResponseBody
    public String generatePayslipCsv(@RequestBody String request) throws Exception {
        return payslipCsvMapper.generatePayslipCsv(request);
    }
}
