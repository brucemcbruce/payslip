package au.net.fell.myob.challenge.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.LocalDate;

@JsonPropertyOrder({"firstName", "lastName", "annualSalary", "superRate", "paymentDate"})
public class PayslipRequest {

    private String firstName;

    private String lastName;

    private int annualSalary;

    private int superRate;

    private LocalDate paymentDate;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAnnualSalary() {
        return annualSalary;
    }

    public void setAnnualSalary(int annualSalary) {
        this.annualSalary = annualSalary;
    }

    public int getSuperRate() {
        return superRate;
    }

    public void setSuperRate(int superRate) {
        this.superRate = superRate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
