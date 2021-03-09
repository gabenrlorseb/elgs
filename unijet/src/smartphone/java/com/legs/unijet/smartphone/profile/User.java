package com.legs.unijet.smartphone.profile;

import java.util.Objects;

public class User   {
    public String name;
    public String surname;
    String ID;
    String department;
    String universityCampus;
    String gender;
    String dateBorn;
    String email;

    //User sb;


    //User sb;


    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String name, String surname, String ID, String department, String universityCampus, String gender, String dateBorn, String email) {
        // sb();
        this.name = name;
        this.surname = surname;
        this.ID = ID;
        this.department = department;
        this.universityCampus = universityCampus;
        this.gender = gender;
        this.dateBorn = dateBorn;
        this.email=email;


    }

    public User(String name, String surname, String department, String universityCampus, String email) {
        // sb();
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.universityCampus = universityCampus;
        this.email=email;


    }




    public String getID() { return ID; }

    public void setID(String ID) { this.ID = ID; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }
    public String getDepartment() { return department; }

    public void setDepartment (String department) { this.department = department; }
    public String getUniversityCampus() { return universityCampus; }

    public void setUniversityCampus(String universityCampus) { this.universityCampus = universityCampus; }




    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getDateBorn() { return dateBorn; }

    public void setDateBorn(String dateBorn) { this.dateBorn = dateBorn; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        User that = (User) o;
        return ID.equals (that.ID);
    }
    @Override
    public int hashCode() {
        return Objects.hash (ID);
    }




}
