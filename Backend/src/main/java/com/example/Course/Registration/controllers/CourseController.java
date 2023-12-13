package com.example.Course.Registration.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.messaging.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;
import java.util.List;

import com.example.Course.Registration.Services.CourseService;
import com.example.Course.Registration.Services.UserService;
import com.example.Course.Registration.models.ClassTiming;
import com.example.Course.Registration.models.Courses;
import com.example.Course.Registration.models.User;
import com.example.Course.Registration.payload.request.AddCourseRequest;
import com.example.Course.Registration.payload.request.CourseRequest;
import com.example.Course.Registration.payload.response.MessageResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "*")
public class CourseController {

    private String email=null;


    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    private void setAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Handle the case when there is no authenticated user
            email =  null;
            return ;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // Assuming email is stored in the username field
        }
        else{
            email = null;
        } 
    }

    @GetMapping("/getAllCourses")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Courses> getAllCourses() {
        setAuthenticatedUserEmail();
        if (email != null) {
            // System.out.println(email);
            User user = userService.findByEmail(email).orElse(null);
            // for(Courses course: user.getCourses()){
            //     String startTime = course.getstartTime();
            //     String endTime = course.getendTime();
            //     System.out.println(Instant.parse(startTime));
            //     System.out.println(Instant.parse(endTime));
            // }
        }
        return courseService.getCourses();
    }

    @GetMapping("/getCourse")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getCourse(@Valid @RequestBody CourseRequest courseRequest) {
        if(email == null){
            setAuthenticatedUserEmail();
            if(email == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not logged in"));
            }
        }
        if(courseRequest.getCourseCRNS().length == 0){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No courses to get"));
        }

        List<Courses> courses=null;
        for(String CRN: courseRequest.getCourseCRNS())
        {
            Courses course = courseService.getCourseByCRN(Integer.parseInt(CRN));
            if(course == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+CRN +" not found"));
            }
            else{
                if(courses == null){
                    courses = Collections.singletonList(course);
                }
                else{
                    courses.add(course);
                }
            }
        }
        return ResponseEntity.ok().body(courses);

    }



    @PostMapping("/register")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> registerCourse(@Valid @RequestBody CourseRequest registerCourseRequest) {
        if(email == null){
            setAuthenticatedUserEmail();
            if(email == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not logged in"));
            }
        }
        if(registerCourseRequest.getCourseCRNS().length == 0){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No courses to register"));
        }
        for(String CRN: registerCourseRequest.getCourseCRNS())
        {
            Courses course = courseService.getCourseByCRN(Integer.parseInt(CRN));
            if(course == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+CRN +" not found"));
            }
            if(course.getSeats() == 0){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+CRN+" is full"));
            }
            course.setSeats(course.getSeats()-1);
            course.setEnrollment(course.getEnrollment()+1);
            courseService.addCourse(course);

            User user = userService.findByEmail(email).orElse(null);
            if(user == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
            }

            if(user.getCourses().stream().anyMatch(c -> c.getCRN().equals(Integer.parseInt(CRN)))){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+CRN+" already registered"));
            }
            user.getCourses().add(course);
            userService.save(user);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course registered successfully"));
    }
    

    @PostMapping("/drop")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> dropCourse(@Valid @RequestBody CourseRequest dropCourseRequest) {
        if(email == null){
            setAuthenticatedUserEmail();
            if(email == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not logged in"));
            }
        }
        if(dropCourseRequest.getCourseCRNS().length == 0){
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No courses to drop"));
        }
        for(String CRN: dropCourseRequest.getCourseCRNS())
        {
            Courses course = courseService.getCourseByCRN(Integer.parseInt(CRN));
            if(course == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+CRN +" not found"));
            }
            course.setSeats(course.getSeats()+1);
            course.setEnrollment(course.getEnrollment()-1);
            courseService.addCourse(course);

            User user = userService.findByEmail(email).orElse(null);
            if(user == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
            }
            if(!user.getCourses().stream().anyMatch(c -> c.getCRN().equals(Integer.parseInt(CRN)))){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+CRN+" not registered"));
            }
            user.getCourses().removeIf(c -> c.getCRN().equals(Integer.parseInt(CRN)));
            userService.save(user);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course dropped successfully"));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> addCourse(@Valid @RequestBody AddCourseRequest courses)
    {
        for(Courses course: courses.getCourses()){
        //    System.out.println(course.getTitle());
            if(courseService.getCourseByCRN(course.getCRN()) != null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+course.getCRN()+" already exists"));
            }

            if(course.getSeats() == null){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course "+course.getCRN()+" has no seats"));
            }
            System.out.println(course.getClassTiming().getday());
            System.out.println(course.getCRN());
            System.out.println(course.getEnrollment());
            System.out.println(course.getHours());
            System.out.println(course.getInstructor());
            System.out.println(course.getPrerequisite());
            System.out.println(course.getSemester());
            System.out.println(course.getSeats());
            System.out.println(course.getTitle());
            try{
                SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm a");
                Date startTime= inputFormat.parse(course.getClassTiming().getStartTime());
                course.getClassTiming().setStartTime(startTime.toString());
                Date endTime= inputFormat.parse(course.getClassTiming().getEndTime());
                course.getClassTiming().setEndTime(endTime.toString());
            }
            catch(ParseException e){
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid time format"));
            }
            courseService.addCourse(course);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course added successfully"));
    }

}
