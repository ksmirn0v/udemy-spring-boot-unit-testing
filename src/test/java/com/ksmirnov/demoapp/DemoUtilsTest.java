package com.ksmirnov.demoapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DemoUtilsTest {

    private DemoUtils demoUtils;

    @BeforeAll
    static void setUpBeforeAllTests() {
        System.out.println("@BeforeAll is executed");
    }

    @BeforeEach
    void setUpBeforeEachTest() {
        demoUtils = new DemoUtils();
        System.out.println("@BeforeEach is executed");
    }

    @AfterEach
    void tearDownAfterEachTest() {
        demoUtils = null;
        System.out.println("@AfterEach is executed");
    }

    @AfterAll
    static void tearDownAfterAllTests() {
        System.out.println("@AfterAll is executed");
    }

    @Test
    @DisplayName("testing DemoUtils.add() method")
    void testAdd() {

        System.out.println("Running the test: testAdd()");

        assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9), "1+9 must not be 6");
    }

    @Test
    @DisplayName("testing DemoUtils.checkNull() method")
    void testCheckNull() {

        System.out.println("Running the test: testCheckNull()");

        assertNull(demoUtils.checkNull(null), "Object should be null");
        assertNotNull(demoUtils.checkNull("value"), "Object should not be null");
    }

    @Test
    @DisplayName("testing DemoUtils.getStringObject() method")
    void testGetStringObject() {

        System.out.println("Running the test: testGetStringObject()");

        assertSame("string", demoUtils.getStringObject(), "Objects should be the same");
        assertNotSame("different", demoUtils.getStringObject(), "Objects should not be the same");
    }

    @Test
    @DisplayName("testing DemoUtils.isGreater() method")
    void testIsGreater() {

        System.out.println("Running the test: testIsGreater()");

        int a = 10;
        int b = 5;
        assertTrue(demoUtils.isGreater(a, b), "This should return true");
        assertFalse(demoUtils.isGreater(b, a), "This should return false");
    }

    @Test
    @DisplayName("testing DemoUtils.getStringArray() method")
    void testGetStringArray() {

        System.out.println("Running the test: testGetStringArray()");

        String[] stringArray = {"abc", "def", "ghi"};
        assertArrayEquals(stringArray, demoUtils.getStringArray(), "Arrays should be equal");
    }

    @Test
    @DisplayName("testing DemoUtils.getStringList() method")
    void testGetStringList() {

        System.out.println("Running the test: testGetStringList()");

        List<String> stringList = List.of("abc", "def", "ghi");
        assertIterableEquals(stringList, demoUtils.getStringList(), "Iterables should be equal");
        assertLinesMatch(stringList, demoUtils.getStringList(), "Lines should be equal");
    }

    @Test
    @DisplayName("testing DemoUtils.throwException() method")
    void testThrowException() {

        System.out.println("Running the test: testThrowException()");

        assertThrows(Exception.class, () -> { demoUtils.throwException(-1); }, "Should throw an exception");
        assertDoesNotThrow(() -> { demoUtils.throwException(2); }, "Should not throw an exceptiuon");
    }

    @Test
    @DisplayName("testing DemoUtils.checkTimeout() method")
    void testCheckTimeout() {

        System.out.println("Running the test: testCheckTimeout()");

        assertTimeoutPreemptively(Duration.ofSeconds(3), () -> { demoUtils.checkTimeout(); }, "Method should execute within 3 seconds");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method")
    void testMultiply() {

        System.out.println("Running the test: testMultiply()");

        assertEquals(12, demoUtils.multiply(3, 4), "3*4 must be 12");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - disabled")
    @Disabled
    void testMultiplyDisabled() {

        System.out.println("Running the test: testMultiply() - disabled");

        assertEquals(2, demoUtils.multiply(1, 2), "1*2 must be 2");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - windows")
    @EnabledOnOs(OS.WINDOWS)
    void testMultiplyWindowsOnly() {

        System.out.println("Running the test: testMultiply() - windows");

        assertEquals(3, demoUtils.multiply(1, 3), "1*3 must be 3");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - macos")
    @EnabledOnOs(OS.MAC)
    void testMultiplyMacOSOnly() {

        System.out.println("Running the test: testMultiply() - macos");

        assertEquals(4, demoUtils.multiply(1, 4), "1*4 must be 4");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - Java 21")
    @EnabledOnJre(JRE.JAVA_21)
    void testMultiplyJava21() {

        System.out.println("Running the test: testMultiply() - Java 21");

        assertEquals(5, demoUtils.multiply(1, 5), "1*5 must be 5");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - Java 8 to Java 20")
    @EnabledForJreRange(min=JRE.JAVA_8, max=JRE.JAVA_20)
    void testMultiplyJava8ToJava20() {

        System.out.println("Running the test: testMultiply() - Java 8 to Java 20");

        assertEquals(6, demoUtils.multiply(1, 6), "1*6 must be 6");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - system property")
    @EnabledIfSystemProperty(named="RELEASE", matches="STG")
    void testMultiplySystemProperty() {

        System.out.println("Running the test: testMultiply() - system property");

        assertEquals(7, demoUtils.multiply(1, 7), "1*7 must be 7");
    }

    @Test
    @DisplayName("testing DemoUtils.multiply() method - environmental variable")
    @EnabledIfEnvironmentVariable(named="RELEASE", matches="DEV")
    void testMultiplyEnvironmentalVariable() {

        System.out.println("Running the test: testMultiply() - environmental variable");

        assertEquals(8, demoUtils.multiply(1, 8), "1*8 must be 8");
    }
}
