package org.example;

public class Student {
    private String name;
    private String password;
    private StudentPlan studentProgress;

    public Student(String name, String password){
        this.name = name;
        this.password = password;
        studentProgress = new StudentPlan(this);
    }

    public StudentPlan getStudentPlan(){
        return studentProgress;
    }

    public String getStudentName(){
        return name;
    }

    public String getStudentPassword(){
        return password;
    }
}
