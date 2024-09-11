package com.ksmirnov.demoapp.controller;

import com.ksmirnov.demoapp.models.*;
import com.ksmirnov.demoapp.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

	@Autowired
	private Gradebook gradebook;

	@Autowired
	private StudentAndGradeService service;


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getStudents(Model model) {
		Iterable<CollegeStudent> students = service.getGradeBook();
		model.addAttribute("students", students);
		return "index";
	}

	@PostMapping(value = "/")
	public String createStudent(@ModelAttribute("student") CollegeStudent student, Model model) {
		service.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
		Iterable<CollegeStudent> students = service.getGradeBook();
		model.addAttribute("students", students);
		return "index";
	}

	@GetMapping(value = "/delete/student/{id}")
	public String deleteStudent(@PathVariable int id, Model model) {
		if (!service.checkIfStudentIsPresent(id)) {
			return "error";
		}
		service.deleteStudent(id);
		Iterable<CollegeStudent> students = service.getGradeBook();
		model.addAttribute("students", students);
		return "index";
	}

	@GetMapping("/studentInformation/{id}")
	public String studentInformation(@PathVariable int id, Model model) {
		if (!service.checkIfStudentIsPresent(id)) {
			return "error";
		}

		service.configureStudentInformationModel(id, model);

		return "studentInformation";
	}

	@PostMapping("/grades")
	public String createGrade(@RequestParam("grade") double grade, @RequestParam("gradeType") String gradeType, @RequestParam("studentId") int studentId, Model model) {
		if (!service.checkIfStudentIsPresent(studentId)) {
			return "error";
		}

		boolean success = service.createGrade(grade, studentId, gradeType);
		if (!success) {
			return "error";
		}

		service.configureStudentInformationModel(studentId, model);

		return "studentInformation";
	}

	@GetMapping("/grades/{id}/{gradeType}")
	public String deleteGrade(@PathVariable int id, @PathVariable String gradeType, Model model) {

		int studentId = service.deleteGrade(id, gradeType);
		if (studentId == 0) {
			return "error";
		}

		service.configureStudentInformationModel(studentId, model);

		return "studentInformation";
	}
}
