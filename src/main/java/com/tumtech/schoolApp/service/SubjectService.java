package com.tumtech.schoolApp.service;

import com.tumtech.schoolApp.dto.SubjectCreationRequest;
import com.tumtech.schoolApp.response.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface SubjectService {
    ApiResponse getAllSubject(int pageNumber, int pageSize, String sortBy, String sortDirection);
    ApiResponse getAllSubjectThatStudentCanOffer (String schoolNo, int pageNumber, int pageSize, String sortBy, String sortDirection);
    ApiResponse addSubject (MultipartFile file);

}
