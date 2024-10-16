package com.pluralsight;

import java.awt.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.InputMismatchException;
import java.util.regex.Pattern;

/***
 * This class holds methods
 */
public class Method {

    private static String dataFileName = "transactions.csv";
    static ArrayList<Transaction> transactions = getTransactions();
    private static LocalDateTime current = LocalDateTime.now();
    private static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    //method designed to create an array list of transactions
    private static ArrayList<Transaction> getTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {

            //  BufferedWriter bw = new BufferedWriter( new FileWriter(dataFileName));
            //  bw.write("date|time|description|vendor|amount\n");
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
                        // System.out.println("Error parsing amount: " + tokens[4]);
                        //  e.printStackTrace();
                    }
                } else {
                    // Handle the case where the line is malformed
                    //  System.out.println("Invalid transaction format: " + input);
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
    private static void saveTransaction(){
        try {

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
    //method designed to show a Home Screen with options
    public static char displayHomeScreenPrompt(){

        System.out.println("\n Welcome to a beta \"CLI Application\"");
        System.out.println("------------------------------------------------------------");
        System.out.println("                        Home Screen ");
        System.out.println("------------------------------------------------------------");
        System.out.println(" D) Add Deposit \n P) Make Payment \n L) Ledger \n X) Exit");

        while(true) {
            System.out.println("\nEnters [D, P, L, X] to continue ");
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
    public static char displayLedgerPrompt() {
       // System.out.println("\n---------------------------Ledger Screen-----------------------------");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println("                                Ledger ");
        System.out.println("--------------------------------------------------------------------------");
        System.out.println(" A) Entries - Display Account Details" +
                " \n D) Deposits - Display only the entries that are deposits into the account " +
                "\n P) Payments - Display only the Debit entries " +
                "\n R) Reports " +
                "\n H) Home " );
        do {
            System.out.println("\nEnter [A, D, P, R, H] to continue");
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
    public static void displayAllEntries(ArrayList<Transaction> entries) {
        //keep showing display and wait for a response Yes or NO
        do {
            System.out.println("                             Account Entries");
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s |  %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("--------------------------------------------------------------------------");
            for (Transaction entry : entries) {
                System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                        entry.getDate(), entry.getTime(), entry.getDescription(), entry.getVendor(), entry.getAmount());
            }
        } while(!Console.PromptForYesNo("\nGo back?")); //end loop when user inputs Yes

    }
    //method designed to show positive transactions
    public static void displayDeposits(ArrayList<Transaction> deposits){
        //keep showing display and wait for a response Yes or NO
        do {
            //display format
            System.out.println("                                 Deposits");
            System.out.println("--------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s |  %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("--------------------------------------------------------------------------");

            //loop through and display transactions greater than 0
            for (Transaction entry : deposits) {
            if (entry.getAmount() > 0) {
                System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                        entry.getDate(), entry.getTime(), entry.getDescription(), entry.getVendor(), entry.getAmount());
            }
        }
        }  while(!Console.PromptForYesNo("\nGo back?")); //end loop when user inputs Yes
    }

    //method displayed to show negative transactions
    public static void displayDebitEntries(ArrayList<Transaction> entries){
        //keep showing display and wait for a response Yes or NO
        do {
            //display format
            System.out.println("                                 Debits");
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("------------------------------------------------------------------------");
            //loop through and display transactions less than 0
            for (Transaction entry : entries) {
                if (entry.getAmount() < 0) {
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            entry.getDate(), entry.getTime(), entry.getDescription(), entry.getVendor(), entry.getAmount());
                }
            }
        } while (!Console.PromptForYesNo("\nGo back?")); //end loop when user inputs Yes
    }
    //display a screen to allow user to perform custom search
    public static void displayReports(){
        int option;
        //keep showing display while true
        while(true) {
            System.out.println("------------------------------------------------------------------------");
            System.out.println("                           Look Up Report ");
            System.out.println("------------------------------------------------------------------------");
            System.out.println(" 1) Filter by Month to Date " +
                    "\n 2) Filter by Previous Month" +
                    "\n 3) Filter by Year to Date" +
                    "\n 4) Filter by Previous Year" +
                    "\n 5) Filter by Vendor " +
                    "\n 0) Go back - Ledger Display");
            System.out.println("\n Enter [0, 1, 2, 3, 4, 5,]");

                        try {
                            option = Console.PromptForInt();
                            if (option == 0) {
                                System.out.println("Returning");
                                return;
                            }
                            if (option == 1) {
                                showByMonthToDate(transactions);
                            }
                            if (option == 2) {
                                showByPreviousMonth(transactions);
                            }
                            if (option == 3) {
                                showByYearToDate(transactions);
                            }
                            if (option == 4) {
                                showByPreviousYear(transactions);
                            }
                            if (option == 5) {
                                searchByVendor(transactions);
                            } else {
                                System.out.println("Invalid: \n");
                            }
                        }
                        catch (Exception e){
                            System.out.println("Error");
                            Console.input.nextInt();
                        }
        }
    }
    //method designed to prompt user for debit information and save it to the csv file
    public static void accountDebit(){

        //TODO: give user option to make a current or past debit


        //continue to prompt new entries until user enters NO
        do {
            // System.out.println(" \n ---- Debit ----- ");
            System.out.println("------------------------------------------------------------------------");
            System.out.println("                            New Debit ");
            System.out.println("------------------------------------------------------------------------");
            //String date = Console.PromptForString(" Date: ");
            //String time = Console.PromptForString(" Time: ");
            String date = current.format(fmtDate);
            String time = current.format(fmtTime);
            String description = Console.PromptForString(" Description: ");
            String vendor = Console.PromptForString(" Vendor: ");
            float amount = Console.PromptForFloat(" Amount: ");
            Transaction debit = new Transaction(date, time, description, vendor, amount);

            transactions.add(debit);
            saveTransaction();
        } while (Console.PromptForYesNo(" Add another debit? ")); //end loop when user input  no
    }
    //method designed to prompt user for deposit information and save to the csv file
    public static void accountDeposit(){

        //TODO: give user option to make a current or past Deposit

        //continue to prompt new deposit while user enters Yes
        do {
           // System.out.println("\n--- Deposit --- ");
           //System.out.println("-------------------------------");
            System.out.println("------------------------------------------------------------------------");
            System.out.println("                            New Deposit ");
            System.out.println("------------------------------------------------------------------------");

            String date;
            String time;

                if (Console.PromptForYesNo("Is this deposit recent?")) {
                     date = current.format(fmtDate);
                     time = current.format(fmtTime);
                }

           // System.out.println("-------------------------------");
             date = Console.PromptForString(" Date: ");
             time = Console.PromptForString(" Time: ");
           // String date = current.format(fmtDate);
            // String time = current.format(fmtTime);
            String description = Console.PromptForString(" Description: ");
            String vendor = Console.PromptForString(" Vendor: ");
            float amount = Console.PromptForFloat(" Amount: ");
            Transaction deposit = new Transaction(date, time, description, vendor, amount);
            transactions.add(deposit);
            saveTransaction();
        }
        while(Console.PromptForYesNo(" Add another Deposit?")); //end loop when user inputs No
    }
    //method designed to show all transactions from october
    private static void showByMonthToDate(ArrayList<Transaction> dates) {
        //keep showing display and wait for a response Yes or NO
        do {
            //declare range to filter
            LocalDate today = LocalDate.from(current).plusDays(1);  // + 1 to include current date in range
            LocalDate endOfRange = today.withDayOfMonth(1).minusDays(1); // -1 to include 1st day of month in range

            boolean found = false;

            System.out.println("                           M-T-D Entries");
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("------------------------------------------------------------------------");
            // Loop through each transaction and find date in the range
            for (Transaction date : dates) {
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                if (transactionDate.isAfter(endOfRange) && transactionDate.isBefore(today)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No match");
            }
        }
        while(!Console.PromptForYesNo("Would you like to change filter?")); // loop ends when user inputs Yes
    }
    //method designed to show all transactions from september
    private static void showByPreviousMonth(ArrayList<Transaction> dates) {
        //keep showing display and wait for a response Yes or NO
        do {
            //declare range to filter
            //-1 to include sept 1 in range
            LocalDate startOfRange = LocalDate.from(current.withMonth(9)).withDayOfMonth(1).minusDays(1);
            //+1 to include sept 30 in range
            LocalDate endOfRange = LocalDate.from(current.withMonth(9)).withDayOfMonth(30).plusDays(1);

            boolean found = false;

            System.out.println("                           Previous Month's Entries");
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("------------------------------------------------------------------------");
            // Loop through array to find all transactions in range specific
            for (Transaction date : dates) {
                //parse transaction dates string
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if transaction is after Aug 31 and before Oct 1 than print transaction
                if (transactionDate.isAfter(startOfRange) && transactionDate.isBefore(endOfRange)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No match");
            }
        }
        while(!Console.PromptForYesNo("Would you like to change filter?")); // loop ends when user inputs Yes
    }
    // method designed to show all transactions from year 2024
    private static void showByYearToDate(ArrayList<Transaction> dates) {
        //keep showing display and wait for a response Yes or NO
        do {
            //declare range to filter
            //+1 to include current day in range
            LocalDate today = LocalDate.from(current).plusDays(1);
            //-1 to include Jan 1 2024 in range
            LocalDate startOfRange = LocalDate.from(current.withMonth(1)).withDayOfMonth(1).minusDays(1);

            boolean found = false;

            System.out.println("                          Y-T-D Entries");
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("------------------------------------------------------------------------");
            // Loop through array to find all transactions in range specific
            for (Transaction date : dates) {
                //parse transaction dates string
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if transaction is after Aug 31 and before Oct 1 than print transaction
                if (transactionDate.isAfter(startOfRange) && transactionDate.isBefore(today)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No match");
            }
        }
        while(!Console.PromptForYesNo("Would you like to change filter?")); // loop ends when user inputs Yes

    }
    //show all transactions from data in 2023
    private static void showByPreviousYear(ArrayList<Transaction> dates) {
        do {
            //declare range to filter
            //-1 to include sept 1 in range
            LocalDate startOfRange = LocalDate.from(current.withMonth(1)).withDayOfMonth(1).minusDays(1);


            boolean found = false;

            System.out.println("                           Previous Year Entries");
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("------------------------------------------------------------------------");
            // Loop through array to find all transactions in range specific
            for (Transaction date : dates) {
                //parse transaction dates string
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if transaction is after Aug 31 and before Oct 1 than print transaction
                if (transactionDate.isBefore(startOfRange)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                    found = true;
                }
            }
            if (!found) {
                System.out.println("No match");
            }
        }
        while(!Console.PromptForYesNo("Would you like to change filter?")); // loop ends when user inputs Yes



    }
    private static void searchByVendor(ArrayList<Transaction> vendors) {
        //loop while answer is no
        do {
            String search = Console.PromptForString("Enter Vendor: ");

            boolean found = false;
            System.out.println("------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("------------------------------------------------------------------------");
            for (Transaction entry : vendors) {
                if (search.equalsIgnoreCase(entry.getVendor())) {
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            entry.getDate(), entry.getTime(), entry.getDescription(), entry.getVendor(), entry.getAmount());
                    found = true;
                }
            }
            // If no match was found, print "no match" once after the loop
            if (!found) {
                System.out.println("No match found for vendor " + search);
            }
        }
        while(!Console.PromptForYesNo("Would you like to change filter?")); // loop ends when user inputs Yes

    }


}