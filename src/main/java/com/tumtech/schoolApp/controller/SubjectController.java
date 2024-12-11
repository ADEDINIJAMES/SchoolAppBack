package com.tumtech.schoolApp.controller;

import com.tumtech.schoolApp.response.ApiResponse;
import com.tumtech.schoolApp.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/subject")
@CrossOrigin (allowCredentials = "true", origins = "http://localhost:5173")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }
    @GetMapping
    public ResponseEntity <ApiResponse> getAllSubject (@RequestParam ("pageNumber") int pageNumber, @RequestParam ("pageSize") int pageSize, @RequestParam ("sortBy") String sortBy, @RequestParam ("sortDirection") String sortDirection){
        ApiResponse response = subjectService.getAllSubject(pageNumber,pageSize,sortBy,sortDirection);
        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @GetMapping ("/{studentNo}")
    public  ResponseEntity <ApiResponse> getAllSubjectThatStudentCanOffer (@PathVariable String studentNo , @RequestParam ("pageNumber") int pageNumber, @RequestParam ("pageSize") int pageSize, @RequestParam ("sortBy") String sortBy, @RequestParam ("sortDirection") String sortDirection){
        ApiResponse response= subjectService.getAllSubjectThatStudentCanOffer(studentNo,pageNumber,pageSize,sortBy,sortDirection);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    @PostMapping
    public ResponseEntity<ApiResponse> addSubject (@RequestPart("file") MultipartFile file){
        ApiResponse response = subjectService.addSubject(file);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
