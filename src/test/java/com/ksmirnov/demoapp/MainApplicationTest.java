package com.ksmirnov.demoapp;

import com.ksmirnov.demoapp.models.CollegeStudent;
import com.ksmirnov.demoapp.models.StudentGrades;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MainApplicationTest {

    private static int count = 0;

    @Value("${info.app.name}")
    private String appName;

    @Value("${info.app.version}")
    private String appVersion;

    @Value("${info.app.description}")
    private String appDescription;

    @Value("${info.school.name}")
    private String schoolName;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades studentGrades;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    public void beforeEach() {
        count = count + 1;
        System.out.println("Testing: " + appName + " which is " + appDescription + " Version: " + appVersion + ". Execution of test method " + count);
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("eroby@mail.com");
        studentGrades.setMathGradeResults(new ArrayList<>(Arrays.asList(100.0, 85.0, 76.5, 91.75)));
        student.setStudentGrades(studentGrades);
    }

    @Test
    void testAddGradeResultsForStudentGradesEqual() {
        assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    void testAddGradeResultsForStudentGradesNotEqual() {
        assertNotEquals(0.0, studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    void testIsGradeGreaterStudentGraderTrue() {
        assertTrue(studentGrades.isGradeGreater(90, 75));
    }

    @Test
    void testIsGradeGreaterStudentGraderFalse() {
        assertFalse(studentGrades.isGradeGreater(89, 92));
    }

    @Test
    void testCheckNullForStudentGrades() {
        assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()));
    }

    @Test
    void testCreateStudentWithoutGradesInit() {
        CollegeStudent otherStudent = context.getBean("collegeStudent", CollegeStudent.class);
        otherStudent.setFirstname("Chad");
        otherStudent.setLastname("Darby");
        otherStudent.setEmailAddress("cdarby@mail.com");
        assertNotNull(otherStudent.getFirstname());
        assertNotNull(otherStudent.getLastname());
        assertNotNull(otherStudent.getEmailAddress());
        assertNull(studentGrades.checkNull(otherStudent.getStudentGrades()));
    }

    @Test
    void testVerifyStudentsArePrototypes() {
        CollegeStudent otherStudent = context.getBean("collegeStudent", CollegeStudent.class);

        assertNotSame(student, otherStudent);
    }

    @Test
    void testFindGradePointAverage() {
        assertAll(
                () -> assertEquals(353.25, studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults())),
                () -> assertEquals(88.31, studentGrades.findGradePointAverage(student.getStudentGrades().getMathGradeResults()))
        );
    }
}
