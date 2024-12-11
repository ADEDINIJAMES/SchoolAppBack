package com.tumtech.schoolApp.controller;

import com.tumtech.schoolApp.dto.StudentRegDto;
import com.tumtech.schoolApp.dto.SubjectRegister;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.response.ApiResponse;
import com.tumtech.schoolApp.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/students")
@CrossOrigin (allowCredentials = "true", origins = "http://localhost:5173")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }
    @PostMapping
    public ResponseEntity<ApiResponse> registerStudent (@Valid  @RequestBody StudentRegDto studentRegDto, @AuthenticationPrincipal Users users){
        ApiResponse response = studentService.registerStudent(studentRegDto, users);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @DeleteMapping ("/{studentNo}")
    public ResponseEntity<ApiResponse> deleteStudent (@PathVariable String studentNo){
        ApiResponse response = studentService.deleteStudent(studentNo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping("/{studentNo}")
    public ResponseEntity<ApiResponse> getStudent (@PathVariable String studentNo){
        ApiResponse response = studentService.getStudent(studentNo);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PatchMapping("/{studentNo}")
    public  ResponseEntity<ApiResponse> updateStudentInfo  (@PathVariable String studentNo, @Valid @RequestBody StudentRegDto studentRegDto){
        ApiResponse response = studentService.updateStudentInfo(studentNo,studentRegDto);
        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping ("/{studentNo}/subject")
    public  ResponseEntity<ApiResponse> registerStudentForSubject (@PathVariable String studentNo , @Valid @RequestBody Set<Long> request){
        ApiResponse response = studentService.registerSubject(studentNo,request);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @GetMapping
    public ResponseEntity<ApiResponse> getAllStudent  (@RequestParam ("pageNumber") int pageNumber, @RequestParam ("pageSize") int pageSize, @RequestParam ("sortBy") String sortBy, @RequestParam ("sortDirection") String sortDirection){
        ApiResponse response = studentService.getAllStudent(pageNumber, pageSize,sortBy,sortDirection);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PatchMapping("/{studentNo}/subject")
    public ResponseEntity<ApiResponse> updateStudentObject (@PathVariable String studentNo, @RequestBody SubjectRegister subjectRegister){
        ApiResponse response = studentService.updateStudentSubject(studentNo,subjectRegister);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

