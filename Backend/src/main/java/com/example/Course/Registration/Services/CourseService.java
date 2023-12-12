package com.example.Course.Registration.Services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;

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
        // return courseRepository.findByTitle(title);
        List<Courses> res = courseRepository.findByTitle(title);
        return res.size()>0?res:null;
    }

    public Courses getCourseByCRN(Integer CRN){
        List<Courses> res = courseRepository.findByCRN(CRN);
        return res.size()>0?res.get(0):null;
    }

    public List<Courses> getCourseBySemester(String semester){
        return Optional.ofNullable(courseRepository.findBySemester(semester)).orElse(null);
    }

    public Courses getCourseByPrerequisite(String prerequisite){
        // return courseRepository.findByPrerequisite(prerequisite).get(0).orElse(null);
        List<Courses> res = courseRepository.findByPrerequisite(prerequisite);
        return res.size()>0?res.get(0):null;
    }

    public List<Courses> getCourseByInstructor(String instructor){
        // return courseRepository.findByInstructor(instructor);
        List<Courses> res = courseRepository.findByInstructor(instructor);
        return res.size()>0?res:null;
    }

    // public Courses getCourseBySeats(Integer seats){
    //     return courseRepository.findBySeats(seats).get(0);
    // }

    public Courses addCourse(Courses course){
        return courseRepository.save(course);
    }

    

    
}
