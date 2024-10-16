package com.pluralsight;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Main {

    static String dataFileName = "transactions.csv";
    public static ArrayList<Transaction> transactions = getTransactions();

    public static void main(String[] args) throws IOException {
            try {
                char userInput;
               do {
                   //Begin user prompt
                   userInput = displayHomeScreenPrompt();

                   //If user enters D, prompt user for deposit information and saves to csv file
                   if (userInput == 'D') {
                       accountDeposit();
                   }
                   //If user enter P, prompt for debit information and save it to the csv file
                   if (userInput == 'P'){
                       accountDebit();
                   }
                   //If user enters L, display the ledger screen
                   if (userInput == 'L') {
                      do {
                          userInput = displayLedgerPrompt();

                          if (userInput == 'A') {

                              displayAllEntries(transactions);
                          }
                          if (userInput == 'D') {
                              displayRecentDeposit(transactions);
                          }
                          if (userInput == 'P') {
                              displayNegativeEntries(transactions);
                          }
                          if (userInput == 'R') {
                              displayReports(transactions);
                          }
                      }
                      while (userInput != 'H'); //break out of loop and return to home page
                   }
               }
               while (userInput != 'X'); //exit program
            }
            catch (Exception e){
                System.out.println("Error");
                e.printStackTrace();
            }
    }

    //display home page  prompts
    public static char displayHomeScreenPrompt(){

        System.out.println("Welcome to a beta \"CLI Application\" \nSelect from the following options.");
        System.out.println("\n D) Add Deposit \n P) Make Payment \n L) Ledger \n X) Exit");

        while(true) {
            System.out.println("Please enters [D, P, L, X]");
            String command = Console.PromptForString();

            if ( command.equalsIgnoreCase("D")){
                return 'D';
            }
            if (command.equalsIgnoreCase("P")){
                return 'P';
            }
            if (command.equalsIgnoreCase("L")){
                return 'L';
            }
            if (command.equalsIgnoreCase("X")
                    || command.equalsIgnoreCase("EXIT")
                    || command.equalsIgnoreCase("Q")
                    || command.equalsIgnoreCase("QUIT")) {
                return 'X';
            }
            else {
                System.out.println("Invalid.");
            }
        }
    }
    //display ledger screen
    public static char displayLedgerPrompt(){
        System.out.println("Ledger Screen Options");
        System.out.println("\n A) All Display all entries" +
                            " \n D) Deposits - Display only the entries that are deposits into account " +
                            "\n P) Payments - Display only the negative entries " +
                            "\n R) Reports " +
                            "\n H) Home " );
        do{
            System.out.println("Please enter [A, D, P, R, H]");
            String command = Console.PromptForString();

            if ( command.equalsIgnoreCase("A")){
                return 'A';
            }
            if ( command.equalsIgnoreCase("D")){
                return 'D';
            }
            if (command.equalsIgnoreCase("P")){
                return 'P';
            }
            if (command.equalsIgnoreCase("R")){
                return 'R';
            }
            if (command.equalsIgnoreCase("H")){
                return 'H';
            }
            else {
                System.out.println("Invalid.");
            }
        }  while (true);

    }
    //method designed to prompt user for debit information and save it to the csv file
    public static void accountDebit(){
        System.out.println("Enter Debit details: ");
        String date = Console.PromptForString("Date: ");
        String time = Console.PromptForString("Time: " );
        String description =Console.PromptForString("Description: ");
        String vendor =Console.PromptForString("Vendor: ");
        float amount= Console.PromptForFloat("Amount: ");
        Transaction p = new Transaction(date, time, description, vendor, amount);
        transactions.add(p);
        saveTransaction();
    }
    //method designed to prompt user for deposit information and save to the csv file
    public static void accountDeposit(){
        System.out.println("\n New Deposit begins: \n");

        String date = Console.PromptForString("Date:");
        String time = Console.PromptForString("Time:" );
        String description =Console.PromptForString(" Description: ");
        String vendor =Console.PromptForString("Test vendor: ");
        float amount= Console.PromptForFloat("Test amount: ");
        Transaction p = new Transaction(date, time, description, vendor, amount);
        transactions.add(p);
        saveTransaction();
    }
    //display all file details
    public static void displayAllEntries(ArrayList<Transaction> entries) {
        for (Transaction entry : entries) {
            System.out.println(entry.getDate() + " | " +
                    entry.getTime() + " | " +
                    entry.getDescription() + " | " +
                    entry.getVendor() + " | " +
                    entry.getAmount());
        }
    }

    public static void displayRecentDeposit(ArrayList<Transaction> deposits){
        System.out.println("\nDeposits: ");
        for (Transaction entry : deposits) {
            if (entry.getAmount() > 0) {
                System.out.println(entry.getDate() + " | " +
                        entry.getTime() + " | " +
                        entry.getDescription() + " | " +
                        entry.getVendor() + " | " +
                        entry.getAmount());
            }
        }
    }

    public static void displayNegativeEntries(ArrayList<Transaction> entries){
        System.out.println("\nPayments: ");
        for (Transaction entry : entries) {
            if (entry.getAmount() < 0) {
                System.out.println(entry.getDate() + " | " +
                        entry.getTime() + " | " +
                        entry.getDescription() + " | " +
                        entry.getVendor() + " | " +
                        entry.getAmount());
            }
        }
    }

    //display a screen to allow user to perform custom search
    public static void displayReports(ArrayList<Transaction> transactions){
        System.out.println("New screen that allows the user to run a search");
        System.out.println("1) Month to Date " +
                "\n2)Previous Month" +
                " \n3)Year to Date" +
                " \n4)Previous Year \nSeach by Vendor " +
                "\n 0) Go back");

        int command;
        while(true){
            System.out.println("Please enter Command [0, 1, 2, 3, 4, 5,]");
            command = Console.PromptForInt();

            switch (command){
                case 0:
                    System.out.println("Returning to previous screen. \n");
                    return;
                case 1:searchByMonthToDate();
                    break;
                case 2: searchByPreviousMonth();
                break;
                case 3: searchByYearToDate();
                break;
                case 4: searchByPreviousYear();
                break;
                case 5: searchByVendor();
                break;
            }
        }
        // while (command != 0);
    }

    public static void searchByMonthToDate() {
        System.out.println("Enter the month and date");

    }
    public static void searchByPreviousMonth() {

    }
    public static void searchByYearToDate() {

    }
    public static void searchByPreviousYear() {

    }

    public static void searchByVendor() {

    }

    public static ArrayList<Transaction> getTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {


         //   BufferedWriter bw = new BufferedWriter( new FileWriter(dataFileName));
         //   bw.write("date|time|description|vendor|amount\n");
          //  bw.close();


            BufferedReader br = new BufferedReader(new FileReader(dataFileName));

            String input;
            while ((input = br.readLine()) != null) {
                String[] tokens = input.split(Pattern.quote("|"));

                // Check if the tokens array has the expected 5 elements (date, time, description, vendor, amount)
                if (tokens.length == 5) {
                    try {
                        // Parse the transaction data from the tokens
                        String date = tokens[0];
                        String time = tokens[1];
                        String description = tokens[2];
                        String vendor = tokens[3];
                        float amount = Float.parseFloat(tokens[4]);

                        // Add the transaction to the list
                        transactions.add(new Transaction(date, time, description, vendor, amount));
                    } catch (NumberFormatException e) {
                        // Handle the case where amount cannot be parsed as a float
                        System.out.println("Error parsing amount: " + tokens[4]);
                    }
                } else {
                    // Handle the case where the line is malformed
                    System.out.println("Invalid transaction format: " + input);
                }
            }

            br.close();
        } catch (Exception e) {
            System.out.println("ERROR!!");
            e.printStackTrace();
        }
        return transactions;
    }

    //method designed to save a transaction when added
    public static void saveTransaction(){
        try{
            
            FileWriter fw = new FileWriter(dataFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Get the last added transaction (the new one)
            Transaction newTransaction = transactions.get(transactions.size() - 1);

            String data = newTransaction.getDate() + "|" +
                    newTransaction.getTime() + "|" +
                    newTransaction.getVendor() + "|" +
                    newTransaction.getDescription() + "|" +
                    newTransaction.getAmount() + "\n";
            fw.write(data);
            bw.close();
        } catch (Exception e) {
            System.out.println("FILE WRITE ERROR");
        }

    }
}