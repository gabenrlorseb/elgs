package com.legs.unijet.tabletversion.course;

import java.util.ArrayList;

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

    // --Commented out by Inspection (21/09/2021 18:51):public void setName(String name) { this.name = name; }

    public String getName() { return name; }

    // --Commented out by Inspection (21/09/2021 18:51):public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

// --Commented out by Inspection START (21/09/2021 18:51):
//    // --Comme// --Commented out by Inspection (21/09/2021 18:51):nted out by Inspection (21/09/2021 18:51):public String getAcademicYear() { return academicYear; }
//
// --Commented out by Inspection START (21/09/2021 18:51):
////    public void setDepartment(String department) { this.department = department; }
//// --Commented out by Inspection STOP (21/09/2021 18:51)
// --Commented out by Inspection STOP (21/09/2021 18:51)

    public String getDepartment() { return department; }

    public void setEmail(String email) { this.email = email; }

    public String getEmail() { return email; }

    public void setMembers(ArrayList<String> members) { this.members = members; }

    public ArrayList<String> getMembers() { return members; }


}


