package com.ifellowtask;

import java.io.File;

public class Main {
    public static void main(String[] args) {

        String filePath = "C:\\buhs\\IFellowTask-Egor-Smirnov\\data.txt";
        try {
            long startTime = System.currentTimeMillis();
            FileSorter.fileSorter(new File(filePath));
            long timeElapsed = System.currentTimeMillis() - startTime;
            System.out.println("Время: " + timeElapsed / 1000);
        } catch (OutOfMemoryError e) {
            throw new RuntimeException(e);
        }
    }
}