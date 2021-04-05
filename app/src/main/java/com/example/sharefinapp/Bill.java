package com.example.sharefinapp;


import java.util.Date;
import java.util.HashMap;

/*
    Bill object
 */
public class Bill {
    private String  category, description, splitType, name;
    private String photoURI;
    private String groupName;
    private double amountDue, amountPaid;
    private Date createDate, remindDate;
    private String recurring;
    private HashMap<String, Double> billSplit;

    public Bill(String category, String description, String splitType, String name, String groupName, double amountDue, double amountPaid, Date createDate, Date remindDate, HashMap<String, Double> billSplit) {
        this.category = category;
        this.description = description;
        this.splitType = splitType;
        this.name = name;
        this.groupName = groupName;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.createDate = createDate;
        this.remindDate = remindDate;
        this.billSplit = billSplit;
    }

    // needed for compatibility with Firestore
    public Bill () {

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoURI() {
        return photoURI;
    }

    public void setPhotoURI(String photoURI) {
        this.photoURI = photoURI;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public double getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(double amountDue) {
        this.amountDue = amountDue;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(Date remindDate) {
        this.remindDate = remindDate;
    }

    public String isRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    public HashMap<String, Double> getBillSplit() {
        return billSplit;
    }

    public void setBillSplit(HashMap<String, Double> billSplit) {
        this.billSplit = billSplit;
    }
}
