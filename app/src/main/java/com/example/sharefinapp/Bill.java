package com.example.sharefinapp;

import com.google.type.DateTime;

/*
    Bill object
 */
public class Bill {
    private String  category, description, splitType, name;
    private String photoURI;
    private String groupRef;
    private float amountDue, amountPaid;
    private DateTime createDate, remindDate;
    private boolean recurring;
    private String recurringPeriodicity;

    public Bill(String category, String description, String splitType, String name, String groupRef, float amountDue, float amountPaid, DateTime createDate, DateTime remindDate) {
        this.category = category;
        this.description = description;
        this.splitType = splitType;
        this.name = name;
        this.groupRef = groupRef;
        this.amountDue = amountDue;
        this.amountPaid = amountPaid;
        this.createDate = createDate;
        this.remindDate = remindDate;
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

    public String getGroupRef() {
        return groupRef;
    }

    public void setGroupRef(String groupRef) {
        this.groupRef = groupRef;
    }

    public float getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(float amountDue) {
        this.amountDue = amountDue;
    }

    public float getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(float amountPaid) {
        this.amountPaid = amountPaid;
    }

    public DateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(DateTime createDate) {
        this.createDate = createDate;
    }

    public DateTime getRemindDate() {
        return remindDate;
    }

    public void setRemindDate(DateTime remindDate) {
        this.remindDate = remindDate;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getRecurringPeriodicity() {
        return recurringPeriodicity;
    }

    public void setRecurringPeriodicity(String recurringPeriodicity) {
        this.recurringPeriodicity = recurringPeriodicity;
    }
}
