package com.legs.unijet;

public class UserStudent   {
    String name,surname,matricola,dipartimento,ateneo,gender;
    String dateBorn;

    public UserStudent() {
    }

    public UserStudent( String name, String surname, String matricola, String dipartimento, String ateneo, String gender, String dateBorn) {
        super ();
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
}
