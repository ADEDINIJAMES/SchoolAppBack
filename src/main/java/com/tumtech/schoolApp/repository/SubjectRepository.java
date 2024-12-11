package com.tumtech.schoolApp.repository;

import com.tumtech.schoolApp.enums.Category;
import com.tumtech.schoolApp.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByCategory(Category category);
    List<Subject> findByIsCompulsory(Boolean isCompulsory);
    List<Subject> findByCategoryAndIsCompulsory(Category category, Boolean isCompulsory);
    Boolean existsByNameIgnoreCase (String name);
}
