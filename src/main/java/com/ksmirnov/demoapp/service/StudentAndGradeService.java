package com.ksmirnov.demoapp.service;

import com.ksmirnov.demoapp.models.*;
import com.ksmirnov.demoapp.repository.HistoryGradeDAO;
import com.ksmirnov.demoapp.repository.MathGradeDAO;
import com.ksmirnov.demoapp.repository.ScienceGradeDAO;
import com.ksmirnov.demoapp.repository.StudentDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentAndGradeService {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private MathGradeDAO mathGradeDAO;

    @Autowired
    private ScienceGradeDAO scienceGradeDAO;

    @Autowired
    private HistoryGradeDAO historyGradeDAO;

    @Autowired
    @Qualifier("mathGrades")
    private MathGrade mathGrade;

    @Autowired
    @Qualifier("scienceGrades")
    private ScienceGrade scienceGrade;

    @Autowired
    @Qualifier("historyGrades")
    private HistoryGrade historyGrade;

    @Autowired
    private StudentGrades grades;

    public void createStudent(String firstName, String lastName, String email) {
        CollegeStudent student = new CollegeStudent(firstName, lastName, email);
        student.setId(0);
        studentDAO.save(student);
    }

    public boolean checkIfStudentIsPresent(int id) {
        Optional<CollegeStudent> student = studentDAO.findById(id);
        return student.isPresent();
    }

    public void deleteStudent(int id) {
        if (checkIfStudentIsPresent(id)) {
            studentDAO.deleteById(id);
            mathGradeDAO.deleteByStudentId(id);
            scienceGradeDAO.deleteByStudentId(id);
            historyGradeDAO.deleteByStudentId(id);
        }
    }

    public Iterable<CollegeStudent> getGradeBook() {
        return studentDAO.findAll();
    }

    public boolean createGrade(double grade, int studentId, String gradeType) {
        if (!checkIfStudentIsPresent(studentId)) {
            return false;
        }

        if (grade < 0 || grade > 100) {
            return false;
        }

        if (gradeType.equals("math")) {
            mathGrade.setId(0);
            mathGrade.setGrade(grade);
            mathGrade.setStudentId(studentId);
            mathGradeDAO.save(mathGrade);
            return true;
        } else if (gradeType.equals("science")) {
            scienceGrade.setId(0);
            scienceGrade.setGrade(grade);
            scienceGrade.setStudentId(studentId);
            scienceGradeDAO.save(scienceGrade);
            return true;
        } else if (gradeType.equals("history")) {
            historyGrade.setId(0);
            historyGrade.setGrade(grade);
            historyGrade.setStudentId(studentId);
            historyGradeDAO.save(historyGrade);
            return true;
        }

        return false;
    }

    public int deleteGrade(int gradeId, String gradeType) {

        int studentId = 0;

        if (gradeType.equals("math")) {
            Optional<MathGrade> grade = mathGradeDAO.findById(gradeId);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            mathGradeDAO.deleteById(gradeId);
        } else if (gradeType.equals("science")) {
            Optional<ScienceGrade> grade = scienceGradeDAO.findById(gradeId);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            scienceGradeDAO.deleteById(gradeId);
        } else if (gradeType.equals("history")) {
            Optional<HistoryGrade> grade = historyGradeDAO.findById(gradeId);
            if (!grade.isPresent()) {
                return studentId;
            }
            studentId = grade.get().getStudentId();
            historyGradeDAO.deleteById(gradeId);
        }

        return studentId;
    }

    public GradebookCollegeStudent getStudentInformation(int id) {

        Optional<CollegeStudent> student = studentDAO.findById(id);
        if (student.isEmpty()) {
            return null;
        }
        Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(id);
        Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(id);
        Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(id);

        List<Grade> mathGradeList = new ArrayList<>();
        mathGrades.forEach(mathGradeList :: add);
        List<Grade> scienceGradeList = new ArrayList<>();
        scienceGrades.forEach(scienceGradeList :: add);
        List<Grade> historyGradeList = new ArrayList<>();
        historyGrades.forEach(historyGradeList :: add);

        grades.setMathGradeResults(mathGradeList);
        grades.setScienceGradeResults(scienceGradeList);
        grades.setHistoryGradeResults(historyGradeList);

        GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
                student.get().getId(),
                student.get().getFirstname(),
                student.get().getLastname(),
                student.get().getEmailAddress(),
                grades
        );

        return gradebookCollegeStudent;
    }

    public void configureStudentInformationModel(int id, Model model) {

        GradebookCollegeStudent student = getStudentInformation(id);
        model.addAttribute("student", student);

        if (student.getStudentGrades().getMathGradeResults().size() > 0) {
            model.addAttribute("mathAverage", student.getStudentGrades().findGradePointAverage(student.getStudentGrades().getMathGradeResults()));
        } else {
            model.addAttribute("mathAverage", "N/A");
        }

        if (student.getStudentGrades().getScienceGradeResults().size() > 0) {
            model.addAttribute("scienceAverage", student.getStudentGrades().findGradePointAverage(student.getStudentGrades().getScienceGradeResults()));
        } else {
            model.addAttribute("scienceAverage", "N/A");
        }

        if (student.getStudentGrades().getHistoryGradeResults().size() > 0) {
            model.addAttribute("historyAverage", student.getStudentGrades().findGradePointAverage(student.getStudentGrades().getHistoryGradeResults()));
        } else {
            model.addAttribute("historyAverage", "N/A");
        }
    }
}
