package com.tumtech.schoolApp.serviceImp;

import com.tumtech.schoolApp.dto.StudentRegDto;
import com.tumtech.schoolApp.dto.StudentResponseDto;
import com.tumtech.schoolApp.dto.SubjectRegister;
import com.tumtech.schoolApp.enums.Category;
import com.tumtech.schoolApp.enums.Gender;
import com.tumtech.schoolApp.enums.Role;
import com.tumtech.schoolApp.exception.UserNotFoundException;
import com.tumtech.schoolApp.model.StudentSubject;
import com.tumtech.schoolApp.model.Subject;
import com.tumtech.schoolApp.model.Users;
import com.tumtech.schoolApp.repository.StudentSubjectRepository;
import com.tumtech.schoolApp.repository.SubjectRepository;
import com.tumtech.schoolApp.repository.UserRepository;
import com.tumtech.schoolApp.response.ApiResponse;
import com.tumtech.schoolApp.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {
    @Autowired
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final StudentSubjectRepository subjectRegisterRepository;
Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);
    public StudentServiceImpl(UserRepository userRepository, SubjectRepository subjectRepository, StudentSubjectRepository subjectRegisterRepository) {
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.subjectRegisterRepository = subjectRegisterRepository;
    }

    @Override
    public ApiResponse registerStudent(StudentRegDto studentRegDto, Users users) {
        try {

if(users.getRole().equals(Role.ADMIN)){
    String className = studentRegDto.getClassOfRegister().substring(0,2);
    String year = String.valueOf(Year.now().getValue());
    String schoolNo = generateSchoolNo(className,year);
    Users student = new Users();
    student.setStudentNo(schoolNo);
    student.setEmail(studentRegDto.getEmail());
    student.setClassOfRegister(studentRegDto.getClassOfRegister());
    student.setFirstName(studentRegDto.getFirstName());
    student.setLastName(studentRegDto.getLastName());
    student.setCategory(Category.valueOf(studentRegDto.getCategory()));
    student.setRole(Role.STUDENT);
    student.setGuardianFirstName(studentRegDto.getGuardianFirstName());
    student.setGuardianLastName(studentRegDto.getGuardianLastName());
    student.setDateOfBirth(studentRegDto.getDateOfBirth());
    student.setGender(Gender.valueOf(studentRegDto.getGender().toUpperCase()));
    student.setDateOfRegister(LocalDate.now());
    student.setPhone(studentRegDto.getPhone());
    Users savedStudent = userRepository.save(student);
    Map<String, Object> studentData = new HashMap<>();
    studentData.put("name", savedStudent.getLastName() +" "+ savedStudent.getFirstName());
    studentData.put("StudentNo", savedStudent.getStudentNo());
    studentData.put("Class", savedStudent.getClassOfRegister());
    studentData.put("Date Of Registration", String.valueOf(savedStudent.getDateOfRegister()));
    studentData.put("Date Of birth", String.valueOf(savedStudent.getDateOfBirth()));
    studentData.put("Category", savedStudent.getCategory());
    ApiResponse apiResponse = new ApiResponse();
    apiResponse.setMessage("Registration done Successfully");
    apiResponse.setData(studentData);
    apiResponse.setStatusCode(201);
    return apiResponse;
}

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("YOU ARE NOT PERMITTED");
            apiResponse.setData(null);
            apiResponse.setStatusCode(403);
            return apiResponse;
    }catch (Exception e){
e.printStackTrace();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setMessage("An error occurred");
            apiResponse.setData(null);
            apiResponse.setStatusCode(500);
            return apiResponse;
        }

    }
  private String  generateSchoolNo(String className, String year){
      SecureRandom secureRandom = new SecureRandom();
      int randomNumber = 100000 + secureRandom.nextInt(900000);
      return className +year+randomNumber;
  }


    @Override
    public ApiResponse deleteStudent(String schoolNo) {
        try{
Users student = userRepository.findByStudentNo(schoolNo).orElseThrow(()-> new UserNotFoundException(" Student not found !!"));
userRepository.delete(student);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(200);
        apiResponse.setData(null);
        apiResponse.setMessage("Student with "+ student.getStudentNo() + " "+  "has been deleted successfully !!" );
        return apiResponse;
    }catch (Exception e){
            e.printStackTrace();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(500);
            apiResponse.setData(null);
            apiResponse.setMessage("An error occurred in the process !!" );
            return apiResponse;
        }
    }

    @Override
    public ApiResponse getStudent(String schoolNo) {
        try{
        Users student = userRepository.findByStudentNo(schoolNo).orElseThrow(()-> new UserNotFoundException(" Student not found !!"));
StudentResponseDto studentResponseDto = mapToStudentResponseDto(student);
Map<String, Object> studentResponse = new HashMap<>();
studentResponse.put("Student Details", studentResponseDto);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(200);
        apiResponse.setData(studentResponse);
        apiResponse.setMessage("Student's details fetched successfully !!!" );
        return apiResponse;

    }catch (Exception e){
        e.printStackTrace();
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(500);
            apiResponse.setData(null);
            apiResponse.setMessage("An error occurred in the process !!" );
            return apiResponse;
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public ApiResponse updateStudentInfo(String schoolNo, StudentRegDto studentRegDto) {
        try {
            Users student = userRepository.findByStudentNo(schoolNo)
                    .orElseThrow(() -> new UserNotFoundException("Student Not Found!!"));

            updateStudentDetails(student, studentRegDto);

            Users updatedStudent = userRepository.save(student);
            StudentResponseDto studentResponseDto = mapToStudentResponseDto(updatedStudent);

            Map<String, Object> updatedStudentDetails = Map.of("Updated Student Details", studentResponseDto);

            return new ApiResponse(201, "User details updated successfully!",updatedStudentDetails);
        } catch (IllegalArgumentException e) {
            return new ApiResponse(400,  "Invalid input provided: " + e.getMessage(), null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse(500,"An error occurred during the update process.", null);
        }
    }

    private void updateStudentDetails(Users student, StudentRegDto studentRegDto) {
        student.setEmail(studentRegDto.getEmail());
        student.setDateOfBirth(studentRegDto.getDateOfBirth());
        student.setFirstName(studentRegDto.getFirstName());
        student.setLastName(studentRegDto.getLastName());
        student.setGuardianFirstName(studentRegDto.getGuardianFirstName());
        student.setGuardianLastName(studentRegDto.getGuardianLastName());
        student.setGender(Gender.valueOf(studentRegDto.getGender().toUpperCase()));
        student.setPhone(studentRegDto.getPhone());

        if (!student.getClassOfRegister().equalsIgnoreCase(studentRegDto.getClassOfRegister())) {
            updateStudentClass(student, studentRegDto.getClassOfRegister());
        }

        if (Category.valueOf(studentRegDto.getCategory().toUpperCase()) != student.getCategory()) {
            // Update the category
            student.setCategory(Category.valueOf(studentRegDto.getCategory().toUpperCase()));

            // Delete subjects for this student using the custom query
            subjectRegisterRepository.deleteByStudentId(student.getId());
            logger.info("Deleted all subjects for student with ID: " + student.getId());

            // Clear the subject collection and update
            student.getSubjectOffered().clear();
            Set<StudentSubject> subjectSet = new HashSet<>();
            student.setSubjectOffered(subjectSet);

            // Save the updated student
            userRepository.save(student);
            logger.info("Subjects cleared for student with ID: " + student.getId());
        }
    }


    private void updateStudentClass(Users student, String newClassOfRegister) {
        student.setClassOfRegister(newClassOfRegister);
        String className = newClassOfRegister.substring(0, 1);
        String year = String.valueOf(student.getDateOfRegister().getYear());
        student.setStudentNo(generateSchoolNo(className, year));
    }


    @Override
    public ApiResponse updateStudentCategory(String schoolNo) {
        return null;
    }

    @Override

    public ApiResponse registerSubject(String schoolNo, Set<Long>request) {
        try {

            if (request!= null) {
                Users student = userRepository.findByStudentNo(schoolNo).orElseThrow(() -> new UserNotFoundException("Student not found "));
                int count=subjectRegisterRepository.findByStudent(student).size();
                if( count>=8&& (count+ request.size()>9)){
                    ApiResponse apiResponse = new ApiResponse();
                    apiResponse.setStatusCode(401);
                    apiResponse.setData(null);
                    apiResponse.setMessage("Selected subject cannot be added to already existing once !!");
                    return apiResponse;
                }
                List<Subject> categorySubjects = subjectRepository.findByCategory(student.getCategory());
                List<Subject> compulsorySubject = subjectRepository.findByIsCompulsory(true);
                Set<Long> compulsoryIds = compulsorySubject.stream()
                        .map(Subject::getId)
                        .collect(Collectors.toSet());
                if (!new HashSet<>(request).containsAll(compulsoryIds)) {
                    throw new IllegalArgumentException("All compulsory subjects must be selected.");
                }

                Set<Long> departmentIds = categorySubjects.stream()
                        .map(Subject::getId)
                        .collect(Collectors.toSet());
                if (!new HashSet<>(request).containsAll(departmentIds)) {
                    throw new IllegalArgumentException("All department-specific subjects must be selected.");
                }
                Set<Long> allRequestedSubject = new HashSet<>(request);

                int totalSubjects = allRequestedSubject.size();
                if (totalSubjects < 8 || totalSubjects > 9) {
                    throw new IllegalArgumentException("Total subjects must be between 8 and 9.");
                }
                List<String> subjectList = new ArrayList<>();
                for (Long subject : allRequestedSubject) {
                    Optional<Subject> oneSubject = subjectRepository.findById(subject);
                    if (oneSubject.isPresent()) {

                        if (!subjectRegisterRepository.existsByStudentAndSubject(student, oneSubject.get())) {
                            StudentSubject studentSubject = new StudentSubject();
                            studentSubject.setStudent(student);
                            studentSubject.setSubject(oneSubject.get());
                            studentSubject.setRegistrationDate(LocalDateTime.now());
                            subjectRegisterRepository.save(studentSubject);
                            subjectList.add(oneSubject.get().getName());
                        }


                        logger.info("Subject {} already registered for {}", subject, student.getStudentNo());
                        continue;
                    }
                    logger.info("Subject {} not found", subject);
                    continue;

                }
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("Registered Subjects", subjectList);
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setStatusCode(201);
                apiResponse.setData(responseData);
                apiResponse.setMessage("Subject Registered Successfully");
                return apiResponse;
            }
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(401);
            apiResponse.setData(null);
            apiResponse.setMessage("Please enter valid details !!");
            return apiResponse;
        } catch (UserNotFoundException e) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(404);
            apiResponse.setData(null);
            apiResponse.setMessage(e.getMessage());
            return apiResponse;

        }catch (IllegalArgumentException e) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(401);
            apiResponse.setData(null);
            apiResponse.setMessage(e.getMessage());
            return apiResponse;

        }catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(500);
            apiResponse.setData(null);
            apiResponse.setMessage("An Error occurred !!!");
            return apiResponse;

        }

    }

    @Override
    public ApiResponse getAllStudent(int pageNumber, int pageSize, String sortBy, String sortDirection) {
        try {
            logger.info("Fetching students with pageNumber: {}, pageSize: {}, sortBy: {}, sortDirection: {}",
                    pageNumber, pageSize, sortBy, sortDirection);

            // Validate sort direction
            Sort sort = "ASC".equalsIgnoreCase(sortDirection) ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();

            // Create pageable object
            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

            // Fetch students by role
            List<Users> allStudents = userRepository.findAllByRole(Role.STUDENT);
            logger.debug("Fetched students: {}", allStudents);

            // Map to DTO
            List<StudentResponseDto> allStudentsPaged = allStudents.stream().map(this::mapToStudentResponseDto).collect(Collectors.toList());
            logger.debug("Mapped students to DTO: {}", allStudentsPaged);
            Page<StudentResponseDto> pagedResp = new PageImpl<>(allStudentsPaged,pageable,allStudentsPaged.size());
            // Prepare response
            Map<String, Object> allStudentResponse = new HashMap<>();
            allStudentResponse.put("students", pagedResp);

            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(200);
            apiResponse.setData(allStudentResponse);
            apiResponse.setMessage("All students fetched successfully!");
            return apiResponse;

        } catch (IllegalArgumentException e) {
            logger.error("Invalid argument provided: {}", e.getMessage());
            return createErrorResponse("Invalid argument provided!", 400);
        } catch (Exception e) {
            logger.error("An error occurred while fetching students", e);
            return createErrorResponse("An error occurred!", 500);
        }
    }

    private ApiResponse createErrorResponse(String message, int statusCode) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(statusCode);
        apiResponse.setData(null);
        apiResponse.setMessage(message);
        return apiResponse;
    }


    @Override
    public ApiResponse updateStudentSubject(String studentNo, SubjectRegister subjectRegister) {
        try{
            if (subjectRegister!= null) {
                Users student = userRepository.findByStudentNo(studentNo).orElseThrow(() -> new UserNotFoundException("Student not found "));
                List<Subject> categorySubjects = subjectRepository.findByCategory(student.getCategory());
                List<Subject> compulsorySubject = subjectRepository.findByIsCompulsory(true);
                Set<Long> compulsoryIds = compulsorySubject.stream()
                        .map(Subject::getId)
                        .collect(Collectors.toSet());
                if (!new HashSet<>(subjectRegister.getSubjectList()).containsAll(compulsoryIds)) {
                    throw new IllegalArgumentException("All compulsory subjects must be selected.");
                }

                Set<Long> departmentIds = categorySubjects.stream()
                        .map(Subject::getId)
                        .collect(Collectors.toSet());
                if (!new HashSet<>(subjectRegister.getSubjectList()).containsAll(departmentIds)) {
                    throw new IllegalArgumentException("All department-specific subjects must be selected.");
                }
                Set<Long> allRequestedSubject = new HashSet<>(subjectRegister.getSubjectList());

                int totalSubjects = allRequestedSubject.size();
                if (totalSubjects < 8 || totalSubjects > 9) {
                    throw new IllegalArgumentException("Total subjects must be between 8 and 9.");
                }
                List<String> subjectList = new ArrayList<>();
                for (Long subject : allRequestedSubject) {
                    Optional<Subject> oneSubject = subjectRepository.findById(subject);
                    if (oneSubject.isPresent()) {

                        if (!subjectRegisterRepository.existsByStudentAndSubject(student, oneSubject.get())) {
                            StudentSubject studentSubject = new StudentSubject();
                            studentSubject.setStudent(student);
                            studentSubject.setSubject(oneSubject.get());
                            studentSubject.setRegistrationDate(LocalDateTime.now());
                            subjectRegisterRepository.save(studentSubject);
                            subjectList.add(oneSubject.get().getName());
                        }
                        logger.info("Subject {} already registered for {}", subject, student.getStudentNo());
                        continue;
                    }
                    logger.info("Subject {} not found", subject);
                    continue;

                }
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("Registered Subjects", subjectList);
                ApiResponse apiResponse = new ApiResponse();
                apiResponse.setStatusCode(201);
                apiResponse.setData(responseData);
                apiResponse.setMessage("Subject Registered Successfully");
                return apiResponse;
            }
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setStatusCode(401);
            apiResponse.setData(null);
            apiResponse.setMessage("Please enter valid details !!");
            return apiResponse;


        } catch (UserNotFoundException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(404);
        apiResponse.setData(null);
        apiResponse.setMessage(e.getMessage());
        return apiResponse;

    }catch (IllegalArgumentException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(401);
        apiResponse.setData(null);
        apiResponse.setMessage(e.getMessage());
        return apiResponse;

    }catch (Exception e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatusCode(500);
        apiResponse.setData(null);
        apiResponse.setMessage("An Error occurred !!!");
        return apiResponse;

    }
    }

    private  StudentResponseDto  mapToStudentResponseDto(Users student){

            StudentResponseDto studentResponseDto = new StudentResponseDto();
            studentResponseDto.setSchoolNo(student.getStudentNo());
studentResponseDto.setLastName(student.getLastName());
studentResponseDto.setPhone(student.getPhone());
            studentResponseDto.setCategory(student.getCategory().toString());
            studentResponseDto.setEmail(student.getEmail());
            studentResponseDto.setClassOfRegister(student.getClassOfRegister());
            studentResponseDto.setFirstName(student.getFirstName());
            studentResponseDto.setDateOfBirth(student.getDateOfBirth());
            studentResponseDto.setDateOfRegister(student.getDateOfRegister());
            studentResponseDto.setGuardianFirstName(student.getGuardianFirstName());
            studentResponseDto.setGuardianLastName(student.getGuardianLastName());
            studentResponseDto.setGender(student.getGender().toString());
            Set<String> subjectNames = new HashSet<>();

Set<StudentSubject> studentsSubjects = student.getSubjectOffered();
if(studentsSubjects.isEmpty()){
    studentResponseDto.setRegisteredSubjects(null);
    return studentResponseDto;
}
for(StudentSubject subject : studentsSubjects){
    subjectNames.add(subject.getSubject().getName());
}
studentResponseDto.setRegisteredSubjects(subjectNames);
        return studentResponseDto;
    }


}
