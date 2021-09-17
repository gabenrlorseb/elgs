package com.legs.unijet.tabletversion.profile;

import java.util.Objects;

public class User   {
    public String name,surname,id,department,universityCampus,gender,dateBorn,email;

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

    public User(String name, String surname, String id, String department, String universityCampus, String gender, String dateBorn, String email) {
        // sb();
        this.name = name;
        this.surname = surname;
        this.id = id;
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


    public String getid() {
        return id;
    }

    public void setid(String id) {
        this.id = id;
    }

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
        return id.equals (that.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash (id);
    }




}
