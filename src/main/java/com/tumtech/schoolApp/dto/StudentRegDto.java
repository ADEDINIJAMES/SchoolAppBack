package com.tumtech.schoolApp.dto;

import com.tumtech.schoolApp.enums.Category;
import com.tumtech.schoolApp.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRegDto {
    @NotBlank(message = "firstName should not be blank")
    private String firstName;
    @NotBlank(message = "lastName  should not be blank")
    private String lastName;
    @NotBlank(message = "email should not be blank")
    private String email;
    @NotBlank(message = "category should not be blank")
    private String category;
    private String guardianFirstName;
    private String guardianLastName;
    @NotBlank (message = "class must be present")
    private String classOfRegister;
    @NotBlank (message = "gender must be present")
    private String gender;
private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private LocalDate dateOfBirth;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGuardianFirstName() {
        return guardianFirstName;
    }

    public void setGuardianFirstName(String guardianFirstName) {
        this.guardianFirstName = guardianFirstName;
    }

    public String getGuardianLastName() {
        return guardianLastName;
    }

    public void setGuardianLastName(String guardianLastName) {
        this.guardianLastName = guardianLastName;
    }

    public String getClassOfRegister() {
        return classOfRegister;
    }

    public void setClassOfRegister(String classOfRegister) {
        this.classOfRegister = classOfRegister;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
