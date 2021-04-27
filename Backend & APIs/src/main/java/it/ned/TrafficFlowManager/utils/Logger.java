package it.ned.TrafficFlowManager.utils;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Logger {
    public static void log(String message) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " " + message);
    }
}
