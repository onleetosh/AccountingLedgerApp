/***
 * "Transaction" class stores and manages information for individual transactions with details such as
 *  date, time, vendor, description, and amount; and provides getter and setter methods to access or modify
 */
package com.pluralsight;

public class Transaction {

    //declared variables
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;   //change to double

    //constructor to initialize a "transaction" object
    public Transaction(String date, String time, String description, String vendor, double amount) {
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
    public double getAmount() { return amount; }

    //setter methods
    public void setDate(String date) { this.date = date; }
    public void setTime(String time) { this.time = time; }
    public void setDescription(String department) { this.description = department;}
    public void setVendor(String vendor) { this.vendor = vendor;}
    public void setAmount(double amount) { this.amount = amount; }
}
