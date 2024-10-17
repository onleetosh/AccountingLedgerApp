package com.pluralsight;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/***
 * "Test" class is used to run test. Not part of the main Project
 */
public class Test {

    private static LocalDateTime current = LocalDateTime.now();
    private static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    public static void main(String[] args) {

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String today = String.valueOf(current);
        //LocalDate start = LocalDate.parse(today, fmtDate);

        String previous = current.minusMonths(1).format(fmt);
        //System.out.println("Today is  " + start);

        LocalDate october = LocalDate.of(2024,10,01);
        String oct1 = october.format(fmtDate);

        System.out.println("Previous Month: " + previous);



        System.out.println("Displaying MTD entries");
        LocalDate today2 = LocalDate.now();

        // Calculate the first day of the month (Month-to-date period)
        LocalDate firstOfMonth = today2.withDayOfMonth(1);


        System.out.println("Get first day of October: " + firstOfMonth);

        LocalDate lastMonth = LocalDate.from(current.withMonth(9)).withDayOfMonth(1).minusDays(1);
        System.out.println("Show previous month" + lastMonth);

        LocalDate endOfRange = LocalDate.from(current.withMonth(9)).withDayOfMonth(30).plusDays(1);
        System.out.println("Show previous month" + endOfRange);

        LocalDate jan1 = LocalDate.from(current.withMonth(1)).withDayOfMonth(1);
        System.out.println("Show first day of year " + jan1);

        LocalDate startOfRange = LocalDate.from(current.withMonth(1)).withDayOfMonth(1).minusDays(1);

        System.out.println("\nTesting out " + startOfRange);

        int x = 20000;
        DecimalFormat formatter = new DecimalFormat("#,###");
        String formattedNumber = formatter.format(x);
        System.out.println(formattedNumber);

        double amount = 1000;
        String amt = String.valueOf(amount);

        System.out.println("number:" + amount);
        System.out.println("string : " +amt);

    }

    private static void customFilter(ArrayList<Transaction> customSearch){
        do {
            //double amount = 0;
            System.out.println("\n Declaring filter...\n");
            String past = Console.PromptForString(" Start (Past) Date: "); //working
            String present = Console.PromptForString(" End (Present) Date: ");//working
            String description = Console.PromptForString(" Description: "); //working
            String vendor = Console.PromptForString(" Vendor: "); //working
            // String amount = Console.PromptForString("Amount: "); //fix
            //double amount = Console.PromptForDouble(" Amount (0 to skip): ");

            /*
            // convert string input to date objects
            LocalDate pastDate = null; //default to empty
            LocalDate presentDate = null; //default to empty

            //if past or present is left blank
            if(!past.isBlank()) {
                //convert string (past) input to a date object
                pastDate = LocalDate.parse(past, fmtDate);
            }
            if(!present.isBlank()) {
                //convert string (present) input to a date object
                presentDate = LocalDate.parse(present, fmtDate);
            }

             */

            LocalDate pastDate = LocalDate.parse(past, fmtDate);
            LocalDate presentDate = LocalDate.parse(present, fmtDate);

            boolean found = false;
            System.out.println("---------------------------------------------------------------------------");
            System.out.printf("%10s | %10s | %15s | %15s | %8s \n", "date", "time", "description", "vendor", "amount");
            System.out.println("---------------------------------------------------------------------------");

            for ( Transaction transaction: customSearch ) {


                LocalDate transactionDate = LocalDate.parse(transaction.getDate(), fmtDate);

                /***
                 *  convert transaction amount (a double) to a String and compare string value:
                 *      String amount = Console.PromptForString(" Amount: ");
                 *      String transactionAmount = String.valueOf(search.getAmount());
                 *      boolean checkAmount = amount.isBlank() || amount.equalsIgnoreCase(search.getAmount)
                 */

                boolean checkPastDate = past.isBlank() || transactionDate.isAfter(pastDate);
                boolean checkPresentDate = present.isBlank() ||  transactionDate.isBefore(presentDate);
                boolean checkDescription = description.isBlank() || description.equalsIgnoreCase(transaction.getDescription());
                boolean checkVendor = vendor.isBlank() || vendor.equalsIgnoreCase(transaction.getVendor());
                // boolean checkAmount = String.valueOf(amount).isBlank() || String.valueOf(amount).equalsIgnoreCase(String.valueOf(transaction.getAmount()));
                //   boolean checkAmount = amount.isBlank() || amount.equals(transaction.getAmount()); && checkAmount

                if( checkPastDate && checkPresentDate && checkDescription && checkVendor ) {
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
