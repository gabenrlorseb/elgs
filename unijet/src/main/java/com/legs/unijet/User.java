package com.legs.unijet;

import java.util.Objects;

public class User   {
    String name,surname,matricola,dipartimento,ateneo,gender,dateBorn,email;

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

    public User(String name, String surname, String matricola, String dipartimento, String ateneo, String gender, String dateBorn, String email) {
        // sb();
        this.name = name;
        this.surname = surname;
        this.matricola = matricola;
        this.dipartimento = dipartimento;
        this.ateneo = ateneo;
        this.gender = gender;
        this.dateBorn = dateBorn;
        this.email=email;


    }



    public String getMatricola() { return matricola; }

    public void setMatricola(String matricola) { this.matricola = matricola; }
    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }
    public String getDipartimento() { return dipartimento; }

    public void setDipartimento(String dipartimento) { this.dipartimento = dipartimento; }
    public String getAteneo() { return ateneo; }

    public void setAteneo(String ateneo) { this.ateneo = ateneo; }




    public String getGender() { return gender; }

    public void setGender(String gender) { this.gender = gender; }

    public String getDateBorn() { return dateBorn; }

    public void setDateBorn(String dateBorn) { this.dateBorn = dateBorn; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass () != o.getClass ()) return false;
        User that = (User) o;
        return matricola.equals (that.matricola);
    }

    @Override
    public int hashCode() {
        return Objects.hash (matricola);
    }




}
