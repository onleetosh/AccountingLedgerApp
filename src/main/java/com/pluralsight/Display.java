package com.pluralsight;

import java.io.*;

/***
 * "Display" class is designed to provide an interactive menu, allowing a user to
 *  make decisions and perform different actions by calling pre-defined methods,
 *  and is where the program runs
 */

public class Display {

    public static void main(String[] args) throws IOException {
        try {
            char userInput;
            do {
                //Begin user prompt
                userInput = Method.displayHomeScreenPrompt();

                //If user enters D, prompt user for deposit information and saves to csv file
                if (userInput == 'D') {
                    Method.accountDeposit();
                }
                //If user enter P, prompt for debit information and save it to the csv file
                if (userInput == 'P'){
                    Method.accountDebit();
                }
                //If user enters L, display the ledger screen
                if (userInput == 'L') {
                    do {
                        userInput = Method.displayLedgerPrompt();

                        if (userInput == 'A') {
                            Method.displayAllEntries(Method.transactions);
                        }
                        if (userInput == 'D') {
                            Method.displayDeposits(Method.transactions);
                        }
                        if (userInput == 'P') {
                            Method.displayDebitEntries(Method.transactions);
                        }
                        if (userInput == 'R') {
                               Method.displayReports();
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
}
