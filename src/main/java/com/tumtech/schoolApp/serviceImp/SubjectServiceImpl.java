package com.tumtech.schoolApp.serviceImp;

import com.tumtech.schoolApp.dto.SubjectCreationRequest;
import com.tumtech.schoolApp.dto.SubjectResponse;
import com.tumtech.schoolApp.exception.UserNotFoundException;
import com.tumtech.schoolApp.model.Subject;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.repository.SubjectRepository;
import com.tumtech.schoolApp.repository.UserRepository;
import com.tumtech.schoolApp.response.ApiResponse;
import com.tumtech.schoolApp.service.SubjectService;
import com.tumtech.schoolApp.util.CSVRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

import static com.tumtech.schoolApp.enums.Category.GENERAL;

@Service
public class SubjectServiceImpl  implements SubjectService {
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
private final CSVRegistration csvRegistration;
    Logger logger = LoggerFactory.getLogger(SubjectServiceImpl.class);
@Autowired
    public SubjectServiceImpl(UserRepository userRepository, SubjectRepository subjectRepository, CSVRegistration csvRegistration) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
    this.csvRegistration = csvRegistration;
}

    @Override
    public ApiResponse getAllSubject(int pageNumber, int pageSize, String sortBy, String sortDirection) {
    try {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Subject> allSubjects = subjectRepository.findAll(pageable);
        Page<SubjectResponse> allSubjectPaged = allSubjects.map(this::mapToSubjectResponseDto);
        Map<String, Object> subjectResponse = new HashMap<>();
        subjectResponse.put("All Subject", allSubjectPaged);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Subject fetched successfully !!");
        apiResponse.setStatusCode(200);
        apiResponse.setData(subjectResponse);
        return apiResponse;
    }catch (Exception e){
        e.printStackTrace();
        logger.error(e.getMessage());
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(500);
        apiResponse.setMessage("An error Occurred in the process!!");
        apiResponse.setData(null);
        return apiResponse;
    }
    }

    @Override
    public ApiResponse getAllSubjectThatStudentCanOffer(String schoolNo, int pageNumber, int pageSize, String sortBy, String sortDirection) {
        try {
            // Retrieve student based on school number
            Users student = userRepository.findByStudentNo(schoolNo)
                    .orElseThrow(() -> new UserNotFoundException("Student not found !!"));

            // Fetch subjects based on conditions
            List<Subject> compulsoryForAll = subjectRepository.findByIsCompulsory(true);
            List<Subject> departmentSpecific = subjectRepository.findByCategory(student.getCategory());
            List<Subject> optionals = subjectRepository.findByCategoryAndIsCompulsory(GENERAL, false);

            // Combine all subject lists
            Set<Subject> allSubjects = new HashSet<>();
            allSubjects.addAll(compulsoryForAll);
            allSubjects.addAll(departmentSpecific);
            allSubjects.addAll(optionals);

            // Map subjects to DTOs
            List<SubjectResponse> subjectResponses = allSubjects.stream()
                    .map(this::mapToSubjectResponseDto).collect(Collectors.toList());

            // Sort subjects based on provided parameters
            Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            // Paginate the response
            int totalSubjects = allSubjects.size();
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), totalSubjects);
            List<SubjectResponse> pagedSubjects = subjectResponses.subList(start, end);

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("All Subject That Student Can Offer", pagedSubjects);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(200);
            apiResponse.setMessage("All Subject That Student Can Offer fetched successfully !!!");
            apiResponse.setData(response);
            return apiResponse;

        } catch (UserNotFoundException e) {
            logger.error("Student not found: " + e.getMessage());
            return new ApiResponse(404, "Student not found !!", null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error occurred while fetching subjects: " + e.getMessage(), e);
            return new ApiResponse(500, "An error occurred in the process!!", null);
        }
    }


    @Override
    public ApiResponse addSubject(MultipartFile file) {
        try {
            Integer count = 0;
            count = csvRegistration.uploadSubjects(file);
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(201);
            apiResponse.setMessage(count+" "+"Subjects have been registered successfully !!!");
            apiResponse.setData(null);
            return apiResponse;
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(500);
            apiResponse.setMessage("An error occurred !!");
            apiResponse.setData(null);
            return apiResponse;
        }
    }
    private SubjectResponse mapToSubjectResponseDto (Subject subject){
    SubjectResponse subjectResponse = new SubjectResponse();
    subjectResponse.setCategory(subject.getCategory());
    subjectResponse.setId(subject.getId());
    subjectResponse.setName(subject.getName());
    subjectResponse.setCompulsory(subject.isCompulsory());
    return subjectResponse;
    }

}
