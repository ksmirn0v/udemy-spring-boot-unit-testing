package com.ksmirnov.demoapp;

public class FizzBuzz {

    public static String compute(int number) {

        StringBuilder stringBuilder = new StringBuilder();

        if (number % 3 == 0) {
            stringBuilder.append("Fizz");
        }

        if (number % 5 == 0) {
            stringBuilder.append("Buzz");
        }

        if (stringBuilder.isEmpty()) {
            stringBuilder.append(number);
        }

        return stringBuilder.toString();
    }
}
