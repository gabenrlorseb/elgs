package com.legs.unijet.smartphone;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Objects;

public class Course{
    String name;
    String academicYear;
    String department;
    String email;
    ArrayList<String> members;

public Course (){

}


    public Course(String name, String academicYear, String department, String email,
                  ArrayList<String> members) {
        this.name = name;
        this.academicYear = academicYear;
        this.department = department;
        this.email = email;
        this.members = members;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public String getAcademicYear() { return academicYear; }

    public void setDepartment(String department) { this.department = department; }

    public String getDepartment() { return department; }

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return email; }

    public void setMembers(ArrayList<String> members) { this.members = members; }

    public ArrayList<String> getMembers() { return members; }


}


