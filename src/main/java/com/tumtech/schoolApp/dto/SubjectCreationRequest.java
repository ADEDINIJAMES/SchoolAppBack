package com.tumtech.schoolApp.dto;

import com.tumtech.schoolApp.enums.Category;
import jakarta.persistence.Column;
import lombok.Data;

@Data
public class SubjectCreationRequest {
    private String name;
    private Category category;
    @Column(nullable = false)
    private boolean isCompulsory;

    public SubjectCreationRequest() {
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

    public SubjectCreationRequest(String name, Category category, boolean isCompulsory) {
        this.name = name;
        this.category = category;
        this.isCompulsory = isCompulsory;
    }
}
