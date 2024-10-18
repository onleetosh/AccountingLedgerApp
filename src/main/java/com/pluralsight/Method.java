/***
 * "Method" class contains several methods that perform specific actions when called upon
 */

package com.pluralsight;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class Method {

    private static final String dataFileName = "transactions.csv";
    //declare a variable with objects from the array list
    static ArrayList<Transaction> transactions = getTransactions();
    //declare a variable that represents current date and time
    private static final LocalDateTime current = LocalDateTime.now();
    //declare variables and assign format patterns
    private static final DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static final DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");

    //method designed to read the file and stores transactions in array list
    private static ArrayList<Transaction> getTransactions() {
        //declare an empty array list
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            //After creating a file comment out to avoid overwrite previous data
            //BufferedWriter bw = new BufferedWriter( new FileWriter(dataFileName));
            //bw.write("date|time|description|vendor|amount\n");
            //bw.close();
            BufferedReader br = new BufferedReader(new FileReader(dataFileName));
            String inputFile;
            while ((inputFile = br.readLine()) != null) {
                //split tokens
                String[] tokens = inputFile.split(Pattern.quote("|"));

                // Check if the tokens array has the 5 elements
                if (tokens.length == 5) {
                    try {
                        // Parse the transaction data from the tokens
                        String date = tokens[0];
                        String time = tokens[1];
                        String description = tokens[2];
                        String vendor = tokens[3];
                        double amount = Float.parseFloat(tokens[4]);

                        // Add the transaction to the list
                        transactions.add(new Transaction(date, time, description, vendor, amount));
                    } catch (Exception e) {
                        // System.out.println("Error");
                        // e.printStackTrace();
                    }
                }
            }
            br.close(); //close and release transactions
        } catch (Exception e) {
            System.out.println("ERROR!!");
            e.printStackTrace();
        }
        return transactions; //add transactions to list
    }

    //method designed to save a transaction when added
    private static void saveTransaction() {
        try {
            //create a FileWriter to append data to file
            FileWriter fw = new FileWriter(dataFileName, true);
            BufferedWriter bw = new BufferedWriter(fw);

            // Get the most recent transaction in the file
            Transaction newTransaction = transactions.get(transactions.size() - 1);

            //format data as a string
            String data = newTransaction.getDate() + "|" +
                    newTransaction.getTime() + "|" +
                    newTransaction.getVendor() + "|" +
                    newTransaction.getDescription() + "|" +
                    newTransaction.getAmount() + "\n";

            fw.write(data); // Write the transaction data to the file.
            bw.close(); //close and release the date
        } catch (Exception e) {
            System.out.println("FILE WRITE ERROR");
        }
    }

    //method designed to show a Home Screen with prompt details
    public static char displayHomeScreenPrompt(){

        //display header
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("                             Home Screen");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println(" D) Add Deposit \n P) Make Payment \n L) Ledger \n X) Exit ");

        //loop while all conditions are true
        while(true) {

            //prompt user for input
            String command = Console.PromptForString(("\nEnters [D, P, L, X] to continue "));

            //conditions with return values
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
                return 'X'; //quit program
            }
            else {
                System.out.println("Invalid.");
            }
        }
    }

    //method displayed to show Ledger with prompt details
    public static char displayLedgerPrompt() {

        //display header
        System.out.println("---------------------------------------------------------------------------");
        System.out.println("                                 Ledger");
        System.out.println("---------------------------------------------------------------------------");
        System.out.println(" A) Account \n D) Deposits\n P) Payments\n R) Reports \n H) Home  " );

        //loop while all conditions are true
        while(true) {
            //prompt user for input and return the value
            String command = Console.PromptForString(("\n Enter [A, D, P, R, H] to continue "));

            if (command.equalsIgnoreCase("A")){
                return 'A';
            }
            if (command.equalsIgnoreCase("D")){
                return 'D';
            }
            if (command.equalsIgnoreCase("P")){
                return 'P';
            }
            if (command.equalsIgnoreCase("R")){
                return 'R';
            }
            if (command.equalsIgnoreCase("H")){
                return 'H'; //break out of loop and return to home screen
            }
            else {
                System.out.println("Invalid.");
            }
        }
    }

    //method designed to show account details
    public static void displayAllEntries(ArrayList<Transaction> entries) {

        //declare balance the sum of cumulative deposit and cumulative debit
        double balance = accountDepositTotal(transactions) + accountDebitTotal(transactions);
        //stay in current display and wait for a response Yes or NO
        do {
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                               Account");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            //print array list
            for (Transaction entry : entries) {
                System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                        entry.getDate(), entry.getTime(), entry.getDescription(), entry.getVendor(), entry.getAmount());
            }
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("\t                 Account Balance: $%.2f \n", balance);
            System.out.println("---------------------------------------------------------------------------");
        } while(!Console.PromptForYesNo("\nGo back?" )); //end loop when user inputs Yes
    }

    //method designed to filter and show positive transactions
    public static void displayDepositEntries(ArrayList<Transaction> deposits){
        //stay in current display and wait for a response Yes or NO
        do {
            //display headline
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                               Deposits");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            //loop through and display transactions greater than 0
            for (Transaction deposit : deposits) {
                if (deposit.getAmount() > 0) {
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            deposit.getDate(), deposit.getTime(), deposit.getDescription(), deposit.getVendor(), deposit.getAmount());
                }
            }
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("\t           Cumulative Deposit Amount: $%.2f \n", accountDepositTotal(transactions));
            System.out.println("---------------------------------------------------------------------------");
        }  while(!Console.PromptForYesNo("\nGo back? ")); //end loop when user inputs Yes
    }

    //method designed to filter and show negative transactions
    public static void displayDebitEntries(ArrayList<Transaction> debits){
        //keep showing display and wait for a response Yes or NO
        do {
            //display format
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                               Debits");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            //loop through and display transactions less than 0
            for (Transaction debit : debits) {
                if (debit.getAmount() < 0) {
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            debit.getDate(), debit.getTime(), debit.getDescription(), debit.getVendor(), debit.getAmount());
                }
            }
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("\t               Cumulative Debit Amount $%.2f \n", accountDebitTotal(transactions));
            System.out.println("---------------------------------------------------------------------------");

        } while (!Console.PromptForYesNo("\nGo back?" )); //end loop when user inputs Yes
    }

    //method designed to show and prompt user with filter options
    public static void displayReportPrompt(){

        int command;
        //stay in current display while all conditions are true
        while(true) {
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                           Look Up Reports ");
            System.out.println("---------------------------------------------------------------------------");
            System.out.println(" 1) Filter by Month to Date " +
                    "\n 2) Filter by Previous Month" +
                    "\n 3) Filter by Year to Date" +
                    "\n 4) Filter by Previous Year" +
                    "\n 5) Filter by Vendor " +
                    "\n 6) Custom Filter " +
                    "\n 0) Go back to Ledger ");

            try {
                //prompt user for command and return a pre-defined filter
                command = Console.PromptForInt(("\n Enter [0, 1, 2, 3, 4, 5, 6] to continue "));

                if (command == 0) {
                    return;
                }
                if(command == 1) {
                    filterByMonthToDate(transactions);
                }
                if (command == 2){
                    filterByPreviousMonth(transactions);
                }
                if (command == 3) {
                    filterByYearToDate(transactions);
                }
                if (command == 4){
                    filterByPreviousYear(transactions);
                }
                if (command == 5) {
                    filterByVendor(transactions);
                }
                if (command == 6){
                    customFilter(transactions);
                }
                else {
                    System.out.println(" \nInvalid entry");
                }
            }
            //if user enters a string catch error
            catch (Exception e) {
                // e.printStackTrace();  //show errors when caught
                System.out.println("Invalid entry. ");
            }
        }
    }

    //method designed to prompt user for deposit information and save to the csv file
    public static void accountDeposit(){

        //continue to prompt new deposit while user enters Yes
        do {
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                             New Deposit ");
            System.out.println("---------------------------------------------------------------------------");

            //declare variables
            String date, time, description, vendor;
            double amount;

            //determine if a deposit is recent or old
            boolean choice = Console.PromptForYesNo("Is this deposit recent?");
            try {
                //if the user is adding a recent deposit set date and time to current
                if (choice) {
                    date = current.format(fmtDate);
                    time = current.format(fmtTime);
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount: ");
                    Transaction deposit = new Transaction(date, time, description, vendor, amount);
                    System.out.println("                     $" + amount + " deposit ending...");
                    transactions.add(deposit);
                    saveTransaction();
                }
                //if deposit is adding a previous deposit prompt for date and time
                if (!choice) {
                    date = Console.PromptForString(" Date: ");
                    time = Console.PromptForString(" Time: ");
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount: ");
                    Transaction deposit = new Transaction(date, time, description, vendor, amount);
                    System.out.println("                   $" + amount + " deposit pending...");
                    transactions.add(deposit);
                    saveTransaction();
                }
            } catch (Exception e){
                System.out.println("\n ERROR! Deposit can not be process... ");
            }
        }
        while(Console.PromptForYesNo("\nAdd new deposit?")); //end loop when user inputs No
    }

    //method designed to loop through transactions and return deposit total
    private static double accountDepositTotal(ArrayList<Transaction> transaction){
        double total = 0.0;
        //loop through transactions and if i is less than transaction
        for (int i = 0; i < transaction.size(); i++){
            total += transaction.get(i).getAmount(); //add values
        }
        return total;
    }

    //method designed to prompt user for debit information and save it to the csv file
    public static void accountDebit(){

        //continue to prompt new entries until user enters NO
        do {
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                            New Debit ");
            System.out.println("---------------------------------------------------------------------------");

            //declare variables
            String date, time, description, vendor;
            double amount;

            //determine if a debit is recent or old
            boolean choice = Console.PromptForYesNo("Is this payment recent?");

            try {
                //if the user is adding a recent debit set date and time to current
                if (choice) {
                    date = current.format(fmtDate);
                    time = current.format(fmtTime);
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount: ");
                    double negAmount = convertToNegative(amount); //convert from positive to negative
                    Transaction debit = new Transaction(date, time, description, vendor, negAmount);
                    System.out.println("                    $" + negAmount + " Debit pending...");
                    transactions.add(debit);
                    saveTransaction();
                }

                //if deposit is adding a previous debit prompt for date and time
                if (!choice) {
                    date = Console.PromptForString(" Date: ");
                    time = Console.PromptForString(" Time: ");
                    description = Console.PromptForString(" Description: ");
                    vendor = Console.PromptForString(" Vendor: ");
                    amount = Console.PromptForDouble(" Amount : ");
                    double negAmount = convertToNegative(amount); //convert from positive to negative
                    Transaction deposit = new Transaction(date, time, description, vendor, negAmount);
                    System.out.println("                      $" + negAmount + " Debit pending...");
                    transactions.add(deposit);
                    saveTransaction();
                }
            } catch (Exception e){
                System.out.println("\n ERROR! Debit can not be processed... ");
            }
        } while (Console.PromptForYesNo(" \nAdd new debit?")); //end loop when user input  no
    }

    //method designed to make a positive number negative
    private static double convertToNegative(double number){
        if (number > 0) {
            return -number;
        }
        else {
            return number;
        }
    }

    //method designed to loop through transactions and return debit total
    private static double accountDebitTotal(ArrayList<Transaction> transaction){
        double total = 0.0;
        //loop through transactions
        for (int i = 0; i < transaction.size(); i++){
            double amount = transaction.get(i).getAmount();
            //is a transaction is less than 0
            if(amount < 0) {
                total += amount; // add values
            }
        } return total;
    }

    //pre-defined method designed to show all transactions in October up to current date
    private static void filterByMonthToDate(ArrayList<Transaction> dates) {
        //keep showing display and wait for a response Yes or NO
        do {
            // Set start date to current day than add 1 to include current date in filter range
            LocalDate today = LocalDate.from(current).plusDays(1);
            // Set end date to Oct 1 then subtract 1 to include Oct 1st in filter range
            LocalDate endRange = LocalDate.of(2024,10,1).minusDays(1);
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                           Month to Date Entries");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            // Loop through each transaction and find date in the range
            for (Transaction date : dates) {

                //convert String to LocalDate
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if a transaction is after Oct 1st and before current date
                if (transactionDate.isAfter(endRange) && transactionDate.isBefore(today)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                }
            }
        }
        while(!Console.PromptForYesNo("\nGo back? ")); // loop ends when user inputs Yes
    }

    //pre-defined method designed to show all transactions in September
    private static void filterByPreviousMonth(ArrayList<Transaction> dates) {
        //keep showing display and wait for a response Yes or NO
        do {
            //Set start date to Sept 1 than subtract 1 to include sept 1 in the filter range
            LocalDate startRange = LocalDate.of(2024,9,1).minusDays(1);
            //Set end date to Sept 30 than add 1 to include sept 30 in the filter range
            LocalDate endRange = LocalDate.of(2024,9,30).plusDays(1);
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                           Previous Month's Entries");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            // Loop through array to find all transactions in range specific
            for (Transaction date : dates) {
                //convert String to LocalDate
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if transaction is after Sept 1 and before Sept 30
                if (transactionDate.isAfter(startRange) && transactionDate.isBefore(endRange)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                }
            }
        }
        while(!Console.PromptForYesNo("\nGo back? ")); // loop ends when user inputs Yes
    }

    //pre-defined method designed to show all transactions in year 2024 up to current date
    private static void filterByYearToDate(ArrayList<Transaction> dates) {
        //keep showing display and wait for a response Yes or NO
        do {
            // Set start date to current day than add 1 to include current date in filter range
            LocalDate today = LocalDate.from(current).plusDays(1);
            // set end date Jan 1st then subtract 1 to include Jan 1 2024 in the filter range
            LocalDate endRange = LocalDate.of(2024,1 , 1).minusDays(1);
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                          Year to Date Entries");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            // Loop through array to find all transactions in range specific
            for (Transaction date : dates) {
                //convert String to LocalDate
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if transaction is after Jan 1 2024 and before current date
                if (transactionDate.isAfter(endRange) && transactionDate.isBefore(today)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                }
            }
        }
        while(!Console.PromptForYesNo("\nGo back? ")); // loop ends when user inputs Yes
    }

    //pre-defined method designed to show all transactions in 2023
    private static void filterByPreviousYear(ArrayList<Transaction> dates) {
        //loop while response is No
        do {
            // set start date Jan 1st then subtract 1 to include Jan 1 2024 in filter range
            LocalDate startRange = LocalDate.of(2023, 1,1).minusDays(1);
            LocalDate endRange = LocalDate.of(2023, 12,31).plusDays(1);
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                           Previous Year Entries");
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            // Loop through array to find all transactions in range specific
            for (Transaction date : dates) {
                //convert String to LocalDate
                LocalDate transactionDate = LocalDate.parse(date.getDate(), fmtDate);
                //if a transaction is before Jan 1st
                if (transactionDate.isAfter(startRange) && transactionDate.isBefore(endRange)) {
                    // Print the transaction that falls within the range
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            date.getDate(), date.getTime(), date.getDescription(), date.getVendor(), date.getAmount());
                }
            }
        }
        while (!Console.PromptForYesNo("\nGo back? ")); // loop ends when user inputs Yes
    }

    //pre-defined method designed to filter transaction by vendor
    private static void filterByVendor(ArrayList<Transaction> vendors) {
        //loop while response is no
        do {
            //prompt user
            String search = Console.PromptForString("Enter Vendor: ");

            boolean found = false;
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            //loop through array list and return possible match
            for (Transaction entry : vendors) {
                if (search.equalsIgnoreCase(entry.getVendor())) {
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            entry.getDate(), entry.getTime(), entry.getDescription(), entry.getVendor(), entry.getAmount());
                    found = true;
                }
            }
            // If no match was found, print "no match" once after the loop
            if (!found) {
                System.out.println("\nNo match found");
            }
        } while(!Console.PromptForYesNo("\nGo back?")); // loop ends when user inputs Yes
    }

    //method designed to perform a custom search
    private static void customFilter(ArrayList<Transaction> customSearch){
        do {
            //prompt user
            System.out.println("---------------------------------------------------------------------------");
            System.out.println("                            Declaring filter");
            System.out.println("---------------------------------------------------------------------------");
            String start = Console.PromptForString("Start Date: ");
            String end = Console.PromptForString("End Date: ");
            String description = Console.PromptForString("Description: ");
            String vendor = Console.PromptForString("Vendor: ");
            String amount = Console.PromptForString("Amount: ");

            //initialize as null
            LocalDate startDate = null;
            LocalDate endDate = null;

            // Parse only if input is valid and catch a potential error
            if (!start.isBlank()) { //if start date is not blank
                try {
                    //convert String to LocalDate with format
                    startDate = LocalDate.parse(start, fmtDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid start date format!");
                    continue;  // Reset the loop and prompt input
                }
            }

            // Parse only if input is valid and catch a potential error
            if (!end.isBlank()) { //if end date is not blank
                try {
                    //convert String to LocalDate with format
                    endDate = LocalDate.parse(end, fmtDate);
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid end date format!");
                    continue;  // Restart the loop and prompt input
                }
            }

            boolean found = false;
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            //loop through transaction array list
            for ( Transaction transaction: customSearch) {

                //Parse date string to Local Date
                LocalDate transactionDate = LocalDate.parse(transaction.getDate(), fmtDate);

                //if an entry is left blank or an entry is provided for dates, description, vendor and amount
                boolean checkPastDate = start.isBlank() || transactionDate.isAfter(startDate.plusDays(1)) ;
                boolean checkPresentDate = end.isBlank() || transactionDate.isBefore(endDate.minusDays(1)) ;
                boolean checkDescription = description.isBlank() || description.equalsIgnoreCase(transaction.getDescription());
                boolean checkVendor = vendor.isBlank() || vendor.equalsIgnoreCase(transaction.getVendor());
                boolean checkAmount = amount.isBlank() ||
                        //subtract the values and if less than < .01 or > -.01 declare them equal
                        Double.parseDouble(amount) - transaction.getAmount() < 0.01 &&
                                Double.parseDouble(amount) - transaction.getAmount() > -0.01;
                //if any of these conditions are meet
                if( checkPastDate && checkPresentDate && checkDescription && checkVendor  && checkAmount) {
                    System.out.printf("%10s | %10s | %15s | %15s |  $%.2f \n",
                            transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                    found = true;
                }
            }
            // If no match was found, print "no match" once after the loop
            if (!found) {
                System.out.println("\nNo match found");
            }
        } while(!Console.PromptForYesNo("\n Go back?")); // loop ends when user inputs Yes
    }
}
