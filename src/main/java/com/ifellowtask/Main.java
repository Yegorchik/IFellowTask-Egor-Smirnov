package com.ifellowtask;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }
        String filePath = args[0];
        try {
            System.out.println("Сортировка началась");

            FileSorter.fileSorter(new File(filePath));

            System.out.println("Строки отсортированы");
        } catch (OutOfMemoryError e) {
            throw new RuntimeException(e);
        }
    }
}