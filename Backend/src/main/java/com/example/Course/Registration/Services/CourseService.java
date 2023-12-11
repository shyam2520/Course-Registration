package com.example.Course.Registration.Services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.example.Course.Registration.models.Courses;
import com.example.Course.Registration.repository.CourseRepository;
// import com.example.Course.Registration.Repositorxy.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private  CourseRepository courseRepository;

    public List<Courses> getCourses(){
        return courseRepository.findAll();
    }

    public Courses getCourseById(String id){
        return courseRepository.findById(id).orElse(null);
    }

    public List<Courses> getCourseByTitle(String title){
        return courseRepository.findByTitle(title);
    }

    public Courses getCourseByCRN(Integer CRN){
        return courseRepository.findByCRN(CRN).get(0);
    }

    public List<Courses> getCourseBySemester(String semester){
        return courseRepository.findBySemester(semester);
    }

    // public Courses getCourseByHours(Integer hours){
    //     return courseRepository.findByHours(hours).get(0);
    // }

    // public Courses getCourseByEnrollment(String enrollment){
    //     return courseRepository.findByEnrollment(enrollment).get(0);
    // }

    public Courses getCourseByPrerequisite(String prerequisite){
        return courseRepository.findByPrerequisite(prerequisite).get(0);
    }

    public List<Courses> getCourseByInstructor(String instructor){
        return courseRepository.findByInstructor(instructor);
    }

    // public Courses getCourseBySeats(Integer seats){
    //     return courseRepository.findBySeats(seats).get(0);
    // }

    public Courses addCourse(Courses course){
        return courseRepository.save(course);
    }

    

    
}
