package com.tumtech.schoolApp.service;

import com.tumtech.schoolApp.dto.StudentRegDto;
import com.tumtech.schoolApp.dto.SubjectRegister;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.response.ApiResponse;

import java.util.List;
import java.util.Set;

public interface StudentService {
    ApiResponse registerStudent (StudentRegDto studentRegDto, Users users);
    ApiResponse deleteStudent (String schoolNo);
    ApiResponse getStudent (String schoolNo);
    ApiResponse updateStudentInfo (String schoolNo, StudentRegDto studentRegDto);
    ApiResponse updateStudentCategory (String schoolNo);

    ApiResponse registerSubject (String schoolNo, Set<Long> request);
     ApiResponse getAllStudent(int pageNumber, int pageSize, String sortBy, String sortDirection);

     ApiResponse updateStudentSubject (String studentNo, SubjectRegister subjectRegister);
}
