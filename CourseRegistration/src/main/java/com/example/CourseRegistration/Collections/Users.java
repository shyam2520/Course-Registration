package com.example.CourseRegistration.Collections;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

    
@Document(collection = "Users")
public class Users {
    private String name;
    private String email;
    private String password;
    private String branch;
    private String degree;
    private String role;

    public Users(String name, String email, String password, String branch, String degree, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.branch = branch;
        this.degree = degree;
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPassword(String password) { this.password = password; }

    public String getName() {
        return name;
    }

    public String getBranch() {
        return branch;
    }

    public String getDegree() {
        return degree;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() { return password; }
    
}
