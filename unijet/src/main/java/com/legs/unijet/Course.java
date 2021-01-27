package com.legs.unijet;

public class Course {
    String name, department, academicYear;


    public Course(String name, String department, String academicYear) {
        this.name = name;
        this.department = department;
        this.academicYear = academicYear;

    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear (String academicYear) { this.academicYear = academicYear; }
}
