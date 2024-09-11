package com.ksmirnov.demoapp.service;

import com.ksmirnov.demoapp.models.*;
import com.ksmirnov.demoapp.repository.HistoryGradeDAO;
import com.ksmirnov.demoapp.repository.MathGradeDAO;
import com.ksmirnov.demoapp.repository.ScienceGradeDAO;
import com.ksmirnov.demoapp.repository.StudentDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class StudentAndGradesServiceTest {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private MathGradeDAO mathGradeDAO;

    @Autowired
    private ScienceGradeDAO scienceGradeDAO;

    @Autowired
    private HistoryGradeDAO historyGradeDAO;

    @Autowired
    private StudentAndGradeService service;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Value("${sql.scripts.create.student}")
    private String sqlCreateStudent;

    @Value("${sql.scripts.create.math.grade}")
    private String sqlCreateMathGrade;

    @Value("${sql.scripts.create.science.grade}")
    private String sqlCreateScienceGrade;

    @Value("${sql.scripts.create.history.grade}")
    private String sqlCreateHistoryGrade;

    @Value("${sql.scripts.delete.student}")
    private String sqlDeleteStudent;

    @Value("${sql.scripts.delete.math.grade}")
    private String sqlDeleteMathGrade;

    @Value("${sql.scripts.delete.science.grade}")
    private String sqlDeleteScienceGrade;

    @Value("${sql.scripts.delete.history.grade}")
    private String sqlDeleteHistoryGrade;

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.execute(sqlCreateStudent);
        jdbcTemplate.execute(sqlCreateMathGrade);
        jdbcTemplate.execute(sqlCreateScienceGrade);
        jdbcTemplate.execute(sqlCreateHistoryGrade);
    }

    @AfterEach
    public void afterEach() {
        jdbcTemplate.execute(sqlDeleteStudent);
        jdbcTemplate.execute(sqlDeleteMathGrade);
        jdbcTemplate.execute(sqlDeleteScienceGrade);
        jdbcTemplate.execute(sqlDeleteHistoryGrade);
    }

    @Test
    void testCreateStudent() {
        jdbcTemplate.execute(sqlDeleteStudent);
        service.createStudent("John", "Smith", "jsmith@mail.com");
        CollegeStudent student = studentDAO.findByEmailAddress("jsmith@mail.com");
        assertEquals("jsmith@mail.com", student.getEmailAddress());
    }

    @Test
    void testIsStudentPresent() {
        int id = 1;
        assertTrue(service.checkIfStudentIsPresent(id));
        assertFalse(service.checkIfStudentIsPresent(id + 1));
    }

    @Test
    void testDeleteStudent() {

        int studentId = 1;
        int gradeId = 1;

        Optional<CollegeStudent> student = studentDAO.findById(studentId);
        Optional<MathGrade> mathGrade = mathGradeDAO.findById(gradeId);
        Optional<ScienceGrade> scienceGrade = scienceGradeDAO.findById(gradeId);
        Optional<HistoryGrade> historyGrade = historyGradeDAO.findById(gradeId);

        assertTrue(student.isPresent());
        assertTrue(mathGrade.isPresent());
        assertTrue(scienceGrade.isPresent());
        assertTrue(historyGrade.isPresent());

        service.deleteStudent(studentId);

        student = studentDAO.findById(studentId);
        assertFalse(student.isPresent());

        mathGrade = mathGradeDAO.findById(gradeId);
        scienceGrade = scienceGradeDAO.findById(gradeId);
        historyGrade = historyGradeDAO.findById(gradeId);
        assertFalse(mathGrade.isPresent());
        assertFalse(scienceGrade.isPresent());
        assertFalse(historyGrade.isPresent());
    }

    @Sql("/sql/insert_data.sql")
    @Test
    void testGetGradeBook() {
        Iterable<CollegeStudent> studentsIter = service.getGradeBook();
        List<CollegeStudent> students = new ArrayList<>();
        for (CollegeStudent student : studentsIter) {
            students.add(student);
        }
        assertEquals(5, students.size());
    }

    @Test
    void testCreateGrade() {

        jdbcTemplate.execute(sqlDeleteMathGrade);
        jdbcTemplate.execute(sqlDeleteScienceGrade);
        jdbcTemplate.execute(sqlDeleteHistoryGrade);

        int id = 1;

        assertTrue(service.createGrade(80.50, id, "math"));
        Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(id);
        assertEquals(1, ((Collection<MathGrade>) mathGrades).size());
        assertTrue(mathGrades.iterator().hasNext(), "the student has math grades");
        assertEquals(80.50, mathGrades.iterator().next().getGrade());

        assertTrue(service.createGrade(90.50, id, "science"));
        Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(id);
        assertEquals(1, ((Collection<ScienceGrade>) scienceGrades).size());
        assertTrue(scienceGrades.iterator().hasNext(), "the student has science grades");
        assertEquals(90.50, scienceGrades.iterator().next().getGrade());

        assertTrue(service.createGrade(95.50, id, "history"));
        Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(id);
        assertEquals(1, ((Collection<HistoryGrade>) historyGrades).size());
        assertTrue(historyGrades.iterator().hasNext(), "the student has history grades");
        assertEquals(95.50, historyGrades.iterator().next().getGrade());
    }

    @Test
    void testCreateGradeFails() {
        int id = 1;
        assertFalse(service.createGrade(100.50, id, "math"));
        assertFalse(service.createGrade(-0.5, id, "science"));
        assertFalse(service.createGrade(50.0, id + 1, "science"));
        assertFalse(service.createGrade(50.0, id, "literature"));
    }

    @Test
    void testDeleteGrade() {
        assertEquals(1, service.deleteGrade(1, "math"));
        assertEquals(1, service.deleteGrade(1, "science"));
        assertEquals(1, service.deleteGrade(1, "history"));
    }

    @Test
    void testDeleteGradeFails() {
        assertEquals(0, service.deleteGrade(2, "math"));
        assertEquals(0, service.deleteGrade(1, "literature"));
    }

    @Test
    void testGetStudentInformation() {
        int id = 1;
        GradebookCollegeStudent gradebookCollegeStudent = service.getStudentInformation(id);
        assertNotNull(gradebookCollegeStudent);
        assertEquals(id, gradebookCollegeStudent.getId());
        assertEquals("Eric", gradebookCollegeStudent.getFirstname());
        assertEquals("Roby", gradebookCollegeStudent.getLastname());
        assertEquals("eroby@mail.com", gradebookCollegeStudent.getEmailAddress());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size());
        assertEquals(1, gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size());
    }

    @Test
    void testGetStudentInformationFails() {
        int id = 2;
        GradebookCollegeStudent gradebookCollegeStudent = service.getStudentInformation(id);
        assertNull(gradebookCollegeStudent);
    }
}
