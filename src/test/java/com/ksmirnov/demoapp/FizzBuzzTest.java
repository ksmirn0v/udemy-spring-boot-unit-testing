package com.ksmirnov.demoapp;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

    // - if number is divisible by 3, print 'Fizz'
    // - if number is divisible by 5, print 'Buzz'
    // - if number is divisible by 3 and 5, print 'FizzBuzz'

    @Test
    @Order(1)
    void testIsDivisibleBy3() {

        String expectedString = "Fizz";
        assertEquals(expectedString, FizzBuzz.compute(3), "Should return 'Fizz'");
    }

    @Test
    @Order(2)
    void testIsDivisibleBy5() {

        String expectedString = "Buzz";
        assertEquals(expectedString, FizzBuzz.compute(5), "Should return 'Buzz'");
    }

    @Test
    @Order(3)
    void testIsDivisibleBy3And5() {

        String expectedString = "FizzBuzz";
        assertEquals(expectedString, FizzBuzz.compute(15), "Should return 'FizzBuzz'");
    }

    @Test
    @Order(4)
    void testIsNotDivisibleBy3Or5() {

        String expectedString = "1";
        assertEquals(expectedString, FizzBuzz.compute(1), "Should return '1'");
    }

    @ParameterizedTest(name = "value={0}, expected={1}")
    @CsvFileSource(resources = "/data.csv")
    @Order(5)
    void testIsDivisibleWithCsvFileSource(int value, String expected) {
        assertEquals(expected, FizzBuzz.compute(value), "value=" + value + " should return '" + expected + "'");
    }
}
