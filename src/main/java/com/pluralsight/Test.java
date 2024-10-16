package com.pluralsight;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test {

    private static LocalDateTime current = LocalDateTime.now();
    private static DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private static DateTimeFormatter fmtTime = DateTimeFormatter.ofPattern("HH:mm:ss");
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
    }
}
