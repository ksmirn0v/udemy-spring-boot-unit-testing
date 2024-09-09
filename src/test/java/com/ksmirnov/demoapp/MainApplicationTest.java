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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = MainApplication.class)
public class MainApplicationTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    CollegeStudent student;

    @Autowired
    StudentGrades grades;

    @Mock
    private ApplicationDao applicationDAO;

    @InjectMocks
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
    void assertThrowsException() {
        CollegeStudent otherStudent = (CollegeStudent) context.getBean("collegeStudent");
        doThrow(new RuntimeException()).when(applicationDAO).checkNull(otherStudent);
        assertThrows(RuntimeException.class, () -> applicationDAO.checkNull(otherStudent));
        verify(applicationDAO, times(1)).checkNull(otherStudent);
    }

    @Test
    void assertThrowsExceptionOnlyOnce() {
        CollegeStudent otherStudent = (CollegeStudent) context.getBean("collegeStudent");
        when(applicationDAO.checkNull(otherStudent)).thenThrow(new RuntimeException()).thenReturn("no exception");
        assertThrows(RuntimeException.class, () -> applicationDAO.checkNull(otherStudent));
        assertEquals("no exception", applicationDAO.checkNull(otherStudent));
        verify(applicationDAO, times(2)).checkNull(otherStudent);

    }
}
