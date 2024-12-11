package com.tumtech.schoolApp.repository;

import com.tumtech.schoolApp.model.StudentSubject;
import com.tumtech.schoolApp.model.Subject;
import com.tumtech.schoolApp.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StudentSubjectRepository  extends JpaRepository<StudentSubject, Long> {
    Boolean existsByStudentAndSubject (Users student, Subject subject);
    List<StudentSubject> findByStudent(Users students);

    @Modifying
    @Query("DELETE FROM StudentSubject ss WHERE ss.student.id = :studentId")
    void deleteByStudentId(UUID studentId);
}
