package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/***
 * This class is used to run test. Not part of the main Project and should not be used
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


    }
}
