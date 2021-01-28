package com.legs.unijet;

import java.security.SecureRandom;

public class Course {
    String name, department, academicYear, email;

    public Course() {

    }

    public Course(String name, String department, String academicYear, String email) {
        this.name = name;
        this.department = department;
        this.academicYear = academicYear;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }


}


