/***
 * "Display" class is designed to provide an interactive menu, allowing a user to make decisions and perform
 *  different actions by calling pre-defined methods, and is where the program runs
 */
package com.pluralsight;

import java.time.LocalDate;

public class Display {

    public static void main(String[] args) {

            System.out.println("\n            Welcome to Year Up United beta \"CLI Application\"");

        // declare a variable
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
                        //user input not set to prompts called in ledger prompt method
                        userInput = Method.displayLedgerPrompt();

                        if (userInput == 'A') { //call display all method
                            Method.displayAllEntries(Method.transactions);
                        }
                        if (userInput == 'D') { //call display deposit method
                            Method.displayDepositEntries(Method.transactions);
                        }
                        if (userInput == 'P') { //call display debit method
                            Method.displayDebitEntries(Method.transactions);
                        }
                        if (userInput == 'R') { //call report method
                               Method.displayReportPrompt();
                        }
                    } while (userInput != 'H'); //break out of loop and return to home page
                }
            }  while (userInput != 'X'); //exit program
    }
}
