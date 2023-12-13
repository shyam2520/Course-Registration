package com.example.Course.Registration.payload.request;
import jakarta.validation.constraints.NotNull;

public class CourseRequest {
    @NotNull
    private String[] courseCRNS;

    public String[] getCourseCRNS() {
        return courseCRNS;
    }

    public void setCourseCRNS(String[] courseCRNS) {
        this.courseCRNS = courseCRNS;
    }

    
}
