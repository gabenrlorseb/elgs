package com.legs.unijet;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

public class Course{
    String IDCourse, name, department, academicYear, email;
    ArrayList<String> members, IDFeedbacks;

public Course (){

}


    public Course(String name, String academicYear, String department, String email,
                  ArrayList<String> members) {
        this.name = name;
        this.academicYear = academicYear;
        this.department = department;
        this.email = email;
        this.members = members;
        /*this.IDPosts = IDPosts;
        this.IDFeedbacks = IDFeedbacks;*/
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

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> recipients) {
        this.members = members;
    }

    public ArrayList<String> getIDFeedbacks() {
        return IDFeedbacks;
    }

    public void setIDFeedbacks(ArrayList<String> recipients) {
        this.IDFeedbacks = IDFeedbacks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return name.equals(course.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}


