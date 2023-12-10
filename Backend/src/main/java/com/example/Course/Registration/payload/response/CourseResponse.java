package com.example.Course.Registration.payload.response;
import java.util.Set;

public class CourseResponse {
    private String id;
    private String title;
    private Integer CRN;
    private String semester;
    private Integer hours;
    private String enrollment;
    private Set<String> prerequisite; 
    private String instructor;
    private Integer seats;

    public CourseResponse(String id, String title, Integer CRN, String semester, Integer hours, String enrollment, Set<String> prerequisite, String instructor, Integer seats) {
        this.id = id;
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

    public Integer getSeats() {
        return seats;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
