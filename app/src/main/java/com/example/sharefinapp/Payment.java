package com.example.sharefinapp;

import java.util.Date;

public class Payment {
    private String paymentID;
    private double amountPaid;
    private double amountRemaining;
    private Date paymentDate;
    private Date metaLoadDate;
    private String description;

    public Payment(String paymentID, double amountPaid, double amountRemaining, Date paymentDate, Date metaLoadDate, String description) {
        this.paymentID = paymentID;
        this.amountPaid = amountPaid;
        this.amountRemaining = amountRemaining;
        this.paymentDate = paymentDate;
        this.metaLoadDate = metaLoadDate;
        this.description = description;
    }
    public Payment() {}

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public double getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(double amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getMetaLoadDate() {
        return metaLoadDate;
    }

    public void setMetaLoadDate(Date metaLoadDate) {
        this.metaLoadDate = metaLoadDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
