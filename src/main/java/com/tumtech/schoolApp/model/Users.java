package com.tumtech.schoolApp.model;

import com.tumtech.schoolApp.enums.Category;
import com.tumtech.schoolApp.enums.Gender;
import com.tumtech.schoolApp.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column (unique = true)
    private String studentNo;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;
    private String firstName;
    private String lastName;
    private String password;
    @Column (unique = true)
    private String email;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public Users() {
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Enumerated(value = EnumType.STRING)
    private Role role;
    @Enumerated(value = EnumType.STRING)
    private Category category;
    private String guardianFirstName;
    private String guardianLastName;
    @Column (name = "class")
    private String classOfRegister;
    private LocalDate dateOfBirth;
    private LocalDate dateOfRegister;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @OneToMany(mappedBy = "student", cascade = CascadeType.PERSIST)
    private Set<StudentSubject> subjectOffered;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(this.role.name())));}


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }





    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
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

    public LocalDate getDateOfRegister() {
        return dateOfRegister;
    }

    public void setDateOfRegister(LocalDate dateOfRegister) {
        this.dateOfRegister = dateOfRegister;
    }

    public Set<StudentSubject> getSubjectOffered() {
        return subjectOffered;
    }

    public void setSubjectOffered(Set<StudentSubject> subjectOffered) {
        this.subjectOffered = subjectOffered;
    }
}
