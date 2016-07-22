package com.github.crehn.pantarhei.tools;

public class NumberGenerator {

    private static int counter = 10;

    public static int nextInt() {
        return counter++;
    }
}
