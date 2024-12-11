package com.tumtech.schoolApp.dto;

import com.tumtech.schoolApp.enums.Category;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SubjectResponse {
    private Long id;
    private String name;
    private Category category;
    @Column(nullable = false)
    private boolean isCompulsory;

    public SubjectResponse() {
    }

    public SubjectResponse(Long id, String name, Category category, boolean isCompulsory) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isCompulsory = isCompulsory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isCompulsory() {
        return isCompulsory;
    }

    public void setCompulsory(boolean compulsory) {
        isCompulsory = compulsory;
    }
}
