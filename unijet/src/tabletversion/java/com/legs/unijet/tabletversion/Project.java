package com.legs.unijet.tabletversion;

public class Project {
String name, course, group;

public Project(){

}

public Project (String name, String course, String group){
    this.name = name;
    this.course = course;
    this.group = group;

}

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getCourse() {
        return course;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getGroup() {
        return group;
    }
}
