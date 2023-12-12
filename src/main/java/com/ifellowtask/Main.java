package com.ifellowtask;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }
        String filePath = args[0];
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