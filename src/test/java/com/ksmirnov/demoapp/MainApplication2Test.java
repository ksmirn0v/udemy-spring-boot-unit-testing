package com.ksmirnov.demoapp;

import com.ksmirnov.demoapp.dao.ApplicationDao;
import com.ksmirnov.demoapp.models.CollegeStudent;
import com.ksmirnov.demoapp.models.StudentGrades;
import com.ksmirnov.demoapp.service.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = MainApplication.class)
public class MainApplication2Test {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades grades;

    @MockBean
    private ApplicationDao applicationDAO;

    @Autowired
    private ApplicationService applicationService;

    @BeforeEach
    public void beforeEach() {
        student.setFirstname("Eric");
        student.setLastname("Roby");
        student.setEmailAddress("eroby@mail.com");
        student.setStudentGrades(grades);
    }

    @Test
    void assertAddGrades() {
        when(applicationDAO.addGradeResultsForSingleClass(grades.getMathGradeResults())).thenReturn(100.0);
        assertEquals(100.0, applicationService.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));
        verify(applicationDAO, times(1)).addGradeResultsForSingleClass(grades.getMathGradeResults());
    }

    @Test
    void testFindGPA() {
        when(applicationDAO.findGradePointAverage(grades.getMathGradeResults())).thenReturn(88.31);
        assertEquals(88.31, applicationService.findGradePointAverage(student.getStudentGrades().getMathGradeResults()));
        verify(applicationDAO, times(1)).findGradePointAverage(grades.getMathGradeResults());
    }

    @Test
    void testCheckNull() {
        when(applicationDAO.checkNull(grades.getMathGradeResults())).thenReturn(true);
        assertNotNull(applicationService.checkNull(student.getStudentGrades().getMathGradeResults()));
        verify(applicationDAO, times(1)).checkNull(grades.getMathGradeResults());
    }
}










