package com.ksmirnov.demoapp;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoUtilsDisplayNamesIndicativeSentencesTest {

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
    @Order(2)
    void testAdd() {

        System.out.println("Running the test: testAdd()");

        assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
        assertNotEquals(6, demoUtils.add(1, 9), "1+9 must not be 6");
    }

    @Test
    @Order(1)
    void testCheckNull() {

        System.out.println("Running the test: testCheckNull()");

        assertNull(demoUtils.checkNull(null), "Object should be null");
        assertNotNull(demoUtils.checkNull("value"), "Object should not be null");
    }
}
