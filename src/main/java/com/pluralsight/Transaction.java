package com.pluralsight;


public class Transaction {

    //declared variables
    private String date;
    private String time;
    private String description;
    private String vendor;
    private float amount;   //change to double

    //constructor
    public Transaction(String date, String time, String description, String vendor, float amount) {
        this.date = date;
        this.time = time;
        this.vendor = vendor;
        this.description = description;
        this.amount = amount;
    }

    //getter methods
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getDescription() { return description; }
    public String getVendor() { return vendor; }
    public float getAmount() { return amount; }

    //setter methods
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setDescription(String department) { this.description = department;}
    public void setVendor(String vendor) { this.vendor = vendor;}
    public void setAmount(float amount) { this.amount = amount; }
}
