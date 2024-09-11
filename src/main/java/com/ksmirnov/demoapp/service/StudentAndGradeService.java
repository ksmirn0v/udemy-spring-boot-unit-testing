package com.ksmirnov.demoapp.service;

import com.ksmirnov.demoapp.models.*;
import com.ksmirnov.demoapp.repository.HistoryGradeDAO;
import com.ksmirnov.demoapp.repository.MathGradeDAO;
import com.ksmirnov.demoapp.repository.ScienceGradeDAO;
import com.ksmirnov.demoapp.repository.StudentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
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
	StudentGrades studentGrades;

	public void createStudent(String firstname, String lastname, String emailAddress) {
		CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
		student.setId(0);
		studentDAO.save(student);
	}

	public void deleteStudent(int id){
		if (isStudentPresent(id)) {
			studentDAO.deleteById(id);
			mathGradeDAO.deleteByStudentId(id);
			scienceGradeDAO.deleteByStudentId(id);
			historyGradeDAO.deleteByStudentId(id);
		}
	}

	public boolean isStudentPresent(int id){
		Optional<CollegeStudent> student = studentDAO.findById(id);
        return student.isPresent();
    }

	public GradebookCollegeStudent getStudentFullInformation(int id) {
		Optional<CollegeStudent> student = studentDAO.findById(id);

		if (student.isEmpty()) {
			return null;
		}

		Iterable<MathGrade> mathGrades = mathGradeDAO.findGradeByStudentId(id);
		List<Grade> mathGradesList = new ArrayList<>();
		mathGrades.forEach(mathGradesList::add);

		Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findGradeByStudentId(id);
		List<Grade> scienceGradesList = new ArrayList<>();
		scienceGrades.forEach(scienceGradesList::add);

		Iterable<HistoryGrade> historyGrades = historyGradeDAO.findGradeByStudentId(id);
		List<Grade> historyGradesList = new ArrayList<>();
		historyGrades.forEach(historyGradesList::add);

		studentGrades.setMathGradeResults(mathGradesList);
		studentGrades.setScienceGradeResults(scienceGradesList);
		studentGrades.setHistoryGradeResults(historyGradesList);

		GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
				student.get().getId(),
				student.get().getFirstname(),
				student.get().getLastname(),
				student.get().getEmailAddress(),
				studentGrades
		);

		return gradebookCollegeStudent;
	}

	public boolean isGradePresent(int id, String gradeType){
		if (gradeType.equals("math")) {
			Optional<MathGrade> grade = mathGradeDAO.findById(id);
			if (grade.isPresent()) {
				return true;
			}
		}
		if (gradeType.equals("science")) {
			Optional<ScienceGrade> grade = scienceGradeDAO.findById(id);
			if (grade.isPresent()) {
				return true;
			}
		}
		if (gradeType.equals("history")) {
			Optional<HistoryGrade> grade = historyGradeDAO.findById(id);
			if (grade.isPresent()) {
				return true;
			}
		}

		return false;
	}

	public int deleteGrade(int id, String gradeType) {

		int studentId = 0;

		if (gradeType.equals("math")) {
			Optional<MathGrade> grade = mathGradeDAO.findById(id);
			if (grade.isEmpty()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			mathGradeDAO.deleteById(id);
		}

		if (gradeType.equals("science")) {
			Optional<ScienceGrade> grade = scienceGradeDAO.findById(id);
			if (grade.isEmpty()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			scienceGradeDAO.deleteById(id);
		}

		if (gradeType.equals("history")) {
			Optional<HistoryGrade> grade = historyGradeDAO.findById(id);
			if (grade.isEmpty()) {
				return studentId;
			}
			studentId = grade.get().getStudentId();
			historyGradeDAO.deleteById(id);
		}

		return studentId;
	}

	public boolean createGrade(double grade, int studentId, String gradeType) {
		if (grade >= 0 && grade <= 100) {
            switch (gradeType) {
                case "math" -> {
                    mathGrade.setId(0);
                    mathGrade.setGrade(grade);
                    mathGrade.setStudentId(studentId);
                    mathGradeDAO.save(mathGrade);
                    return true;
                }
                case "science" -> {
                    scienceGrade.setId(0);
                    scienceGrade.setGrade(grade);
                    scienceGrade.setStudentId(studentId);
                    scienceGradeDAO.save(scienceGrade);
                    return true;
                }
                case "history" -> {
                    historyGrade.setId(0);
                    historyGrade.setGrade(grade);
                    historyGrade.setStudentId(studentId);
                    historyGradeDAO.save(historyGrade);
                    return true;
                }
            }
        }
		return false;
	}

	public Gradebook getGradebook () {

		Iterable<CollegeStudent> collegeStudents = studentDAO.findAll();

		Iterable<MathGrade> mathGrades = mathGradeDAO.findAll();
		Iterable<ScienceGrade> scienceGrades = scienceGradeDAO.findAll();
		Iterable<HistoryGrade> historyGrades = historyGradeDAO.findAll();

		Gradebook gradebook = new Gradebook();

		for (CollegeStudent collegeStudent : collegeStudents) {
			List<Grade> mathGradesPerStudent = new ArrayList<>();
			List<Grade> scienceGradesPerStudent = new ArrayList<>();
			List<Grade> historyGradesPerStudent = new ArrayList<>();

			for (MathGrade grade : mathGrades) {
				if (grade.getStudentId() == collegeStudent.getId()) {
					mathGradesPerStudent.add(grade);
				}
			}
			for (ScienceGrade grade : scienceGrades) {
				if (grade.getStudentId() == collegeStudent.getId()) {
					scienceGradesPerStudent.add(grade);
				}
			}

			for (HistoryGrade grade : historyGrades) {
				if (grade.getStudentId() == collegeStudent.getId()) {
					historyGradesPerStudent.add(grade);
				}
			}

			studentGrades.setMathGradeResults(mathGradesPerStudent);
			studentGrades.setScienceGradeResults(scienceGradesPerStudent);
			studentGrades.setHistoryGradeResults(historyGradesPerStudent);

			GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
					collegeStudent.getId(),
					collegeStudent.getFirstname(),
					collegeStudent.getLastname(),
					collegeStudent.getEmailAddress(),
					studentGrades
			);

			gradebook.getStudents().add(gradebookCollegeStudent);
		}

		return gradebook;
	}

	public void configureStudentInformationModel(int id, Model m) {

		GradebookCollegeStudent studentEntity = getStudentFullInformation(id);

		m.addAttribute("student", studentEntity);

		if (!studentEntity.getStudentGrades().getMathGradeResults().isEmpty()) {
			m.addAttribute("mathAverage", studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getMathGradeResults()));
		} else {
			m.addAttribute("mathAverage", "N/A");
		}
		if (!studentEntity.getStudentGrades().getScienceGradeResults().isEmpty()) {
			m.addAttribute("scienceAverage", studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getScienceGradeResults()));
		} else {
			m.addAttribute("scienceAverage", "N/A");
		}
		if (!studentEntity.getStudentGrades().getHistoryGradeResults().isEmpty()) {
			m.addAttribute("historyAverage", studentEntity.getStudentGrades().findGradePointAverage(studentEntity.getStudentGrades().getHistoryGradeResults()));
		} else {
			m.addAttribute("historyAverage", "N/A");
		}
	}
}
