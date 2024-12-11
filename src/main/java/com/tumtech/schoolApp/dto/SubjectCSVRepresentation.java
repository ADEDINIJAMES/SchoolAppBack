package com.tumtech.schoolApp.dto;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class SubjectCSVRepresentation {
    @CsvBindByName(column = "name")
    private String name;
    @CsvBindByName(column = "category")
    private String category;
    @CsvBindByName(column = "isCompulsory")
    private String isCompulsory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsCompulsory() {
        return isCompulsory;
    }

    public void setIsCompulsory(String isCompulsory) {
        this.isCompulsory = isCompulsory;
    }

    public SubjectCSVRepresentation(String name, String category, String isCompulsory) {
        this.name = name;
        this.category = category;
        this.isCompulsory = isCompulsory;
    }

    public SubjectCSVRepresentation() {
    }
}
