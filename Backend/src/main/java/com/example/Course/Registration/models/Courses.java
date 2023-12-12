package com.example.Course.Registration.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Set;

@Document(collection = "Courses")
public class Courses {
    @Id
    private String id;
    private String title;
    private Integer CRN;
    private String semester;
    private Integer hours;
    private String enrollment;
    private Set<String> prerequisite; 
    private String instructor;
    private Integer seats;
    private String startTime;
    private String endTime;
    private String classDay;

    public Courses(String title, Integer CRN, String semester, Integer hours, String enrollment, Set<String> prerequisite, String instructor, Integer seats) {
        this.title = title;
        this.CRN = CRN;
        this.semester = semester;
        this.hours = hours;
        this.enrollment = enrollment;
        this.prerequisite = prerequisite;
        this.instructor = instructor;
        this.seats = seats;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCRN(Integer CRN) {
        this.CRN = CRN;
    }

    public void setstartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setendTime(String endTime) {
        this.endTime = endTime;
    }

    public void setclassDay(String classDay) {
        this.classDay = classDay;
    }


    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public void setEnrollment(String enrollment) {
        this.enrollment = enrollment;
    }

    public void setPrerequisite(Set<String> prerequisite) {
        this.prerequisite = prerequisite;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCRN() {
        return CRN;
    }

    public String getSemester() {
        return semester;
    }

    public Integer getHours() {
        return hours;
    }

    public String getEnrollment() {
        return enrollment;
    }

    public Set<String> getPrerequisite() {
        return prerequisite;
    }

    public String getInstructor() {
        return instructor;
    }

    public String getstartTime() {
        return startTime;
    }

    public String getendTime() {
        return endTime;
    }

    public String getclassDay() {
        return classDay;
    }
    
    public Integer getSeats() {
        return seats;
    }


}
