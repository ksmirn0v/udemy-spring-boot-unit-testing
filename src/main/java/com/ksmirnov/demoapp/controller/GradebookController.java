package com.ksmirnov.demoapp.controller;

import com.ksmirnov.demoapp.models.CollegeStudent;
import com.ksmirnov.demoapp.models.Gradebook;
import com.ksmirnov.demoapp.models.GradebookCollegeStudent;
import com.ksmirnov.demoapp.service.StudentAndGradeService;
import com.ksmirnov.demoapp.exceptionhandling.StudentOrGradeErrorResponse;
import com.ksmirnov.demoapp.exceptionhandling.StudentOrGradeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class GradebookController {

    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private Gradebook gradebook;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<GradebookCollegeStudent> getStudents() {
        gradebook = studentService.getGradebook();
        return gradebook.getStudents();
    }


    @GetMapping("/studentInformation/{id}")
    public GradebookCollegeStudent getStudentFullInformation(@PathVariable int id) {
        if (!studentService.isStudentPresent(id)) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }
        return studentService.getStudentFullInformation(id);
    }


    @PostMapping(value = "/")
    public List<GradebookCollegeStudent> createStudent(@RequestBody CollegeStudent student) {
        studentService.createStudent(student.getFirstname(), student.getLastname(), student.getEmailAddress());
        gradebook = studentService.getGradebook();
        return gradebook.getStudents();
    }


    @DeleteMapping("/student/{id}")
    public List<GradebookCollegeStudent> deleteStudent(@PathVariable int id) {
        if (!studentService.isStudentPresent(id)) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        studentService.deleteStudent(id);
        gradebook = studentService.getGradebook();
        return gradebook.getStudents();
    }


    @PostMapping(value = "/grades")
    public GradebookCollegeStudent createGrade(
            @RequestParam("grade") double grade,
            @RequestParam("gradeType") String gradeType,
            @RequestParam("studentId") int studentId
    ) {
        if (!studentService.isStudentPresent(studentId)) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        boolean success = studentService.createGrade(grade, studentId, gradeType);

        if (!success) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        GradebookCollegeStudent student = studentService.getStudentFullInformation(studentId);
        if (student == null) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }

        return student;
    }

    @DeleteMapping("/grades/{id}/{gradeType}")
    public GradebookCollegeStudent deleteGrade(@PathVariable int id, @PathVariable String gradeType) {
        int studentId = studentService.deleteGrade(id, gradeType);
        if (studentId == 0) {
            throw new StudentOrGradeNotFoundException("Student or Grade was not found");
        }
        return studentService.getStudentFullInformation(studentId);
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(StudentOrGradeNotFoundException exc) {

        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<StudentOrGradeErrorResponse> handleException(Exception exc) {

        StudentOrGradeErrorResponse error = new StudentOrGradeErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
