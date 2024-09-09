package com.ksmirnov.demoapp;

import com.ksmirnov.demoapp.dao.ApplicationDao;
import com.ksmirnov.demoapp.models.CollegeStudent;
import com.ksmirnov.demoapp.models.StudentGrades;
import com.ksmirnov.demoapp.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest()
public class MainApplicationTest {

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades grades;

    @BeforeEach
    public void beforeEach() {
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("eroby@mail.com");
        student.setStudentGrades(grades);
        ReflectionTestUtils.setField(student, "id", 10);
        ReflectionTestUtils.setField(student, "studentGrades", new StudentGrades(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.5, 91.75))));
    }

    @Test
    void testId() {
        assertEquals(10, ReflectionTestUtils.getField(student, "id"));
    }

    @Test
    void testGetFirstNameAndId() {
        assertEquals("10: Eric", ReflectionTestUtils.invokeMethod(student, "getFirstNameAndId"));
    }
}
