package au.net.fell.myob.challenge.service;

import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.model.PayslipRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.Math.round;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@Service
public class PayslipFactory {

    private final IncomeTaxCalculator incomeTaxCalculator;

    @Autowired
    public PayslipFactory(IncomeTaxCalculator incomeTaxCalculator) {
        this.incomeTaxCalculator = incomeTaxCalculator;
    }

    public Payslip generatePayslip(PayslipRequest request) {
        Payslip payslip = new Payslip();
        payslip.setName(mapName(request));
        payslip.setPayPeriodStart(mapStartDate(request));
        payslip.setPayPeriodEnd(mapEndDate(request));
        payslip.setGrossIncome(mapGrossIncome(request));
        payslip.setIncomeTax(mapIncomeTax(request));
        payslip.setNetIncome(mapNetIncome(request));
        payslip.setSuperannuation(mapSuper(request));
        return payslip;
    }

    private String mapName(PayslipRequest request) {
        return String.format("%s %s", request.getFirstName(), request.getLastName());
    }

    private LocalDate mapStartDate(PayslipRequest request) {
        LocalDate paymentDate = request.getPaymentDate();
        checkArgument(paymentDate != null, "Payment date must be provided in order to generate payslip");
        return paymentDate.with(firstDayOfMonth());
    }

    private LocalDate mapEndDate(PayslipRequest request) {
        LocalDate paymentDate = request.getPaymentDate();
        checkArgument(paymentDate != null, "Payment date must be provided in order to generate payslip");
        return paymentDate.with(lastDayOfMonth());
    }

    private int mapGrossIncome(PayslipRequest request) {
        return round((float) request.getAnnualSalary() / 12);
    }

    private int mapIncomeTax(PayslipRequest request) {
        return round((float)incomeTaxCalculator.calculate(request.getAnnualSalary())/12);
   }

    private int mapNetIncome(PayslipRequest request) {
        return mapGrossIncome(request) - mapIncomeTax(request);
    }

    private int mapSuper(PayslipRequest request) {
        return round(((float) mapGrossIncome(request) * request.getSuperRate()) / 100);
    }
}
