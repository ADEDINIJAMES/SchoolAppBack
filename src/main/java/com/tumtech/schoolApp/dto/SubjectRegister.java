package com.tumtech.schoolApp.dto;

import com.tumtech.schoolApp.model.Subject;
import lombok.Data;

import java.util.List;
@Data
public class SubjectRegister {
    private List<Long> subjectList;

    public List<Long> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Long> subjectList) {
        this.subjectList = subjectList;
    }

    public SubjectRegister(List<Long> subjectList) {
        this.subjectList = subjectList;
    }

    public SubjectRegister() {
    }

}
