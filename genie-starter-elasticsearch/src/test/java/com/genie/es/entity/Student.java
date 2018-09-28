package com.genie.es.entity;


import com.genie.es.annotation.ESIndex;

@ESIndex(name = "student", shard = true)
public class Student extends Member {

    private String grade;


    public Student(String id, String area) {
        super(id, area);
    }


    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
