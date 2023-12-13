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
import java.util.Comparator;
import java.util.List;

import com.example.Course.Registration.Services.CourseService;
import com.example.Course.Registration.Services.UserService;
import com.example.Course.Registration.models.ClassTiming;
import com.example.Course.Registration.models.Courses;
import com.example.Course.Registration.models.User;
import com.example.Course.Registration.payload.request.AddCourseRequest;
import com.example.Course.Registration.payload.request.CourseRequest;
import com.example.Course.Registration.payload.response.MessageResponse;
import com.mongodb.internal.connection.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class CourseController {

    private String email = null;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    private void setAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            // Handle the case when there is no authenticated user
            email = null;
            return;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // Assuming email is stored in the username field
        } else {
            email = null;
        }
    }

    private static Date parseMongoDbTime(String mongoDbTimeString) {
        try {
            SimpleDateFormat mongoDbDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            return convertToTimeZone(mongoDbDateFormat.parse(mongoDbTimeString),TimeZone.getTimeZone("America/New_York"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Method to convert to a specific timezone
    private static Date convertToTimeZone(Date date, TimeZone timeZone) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        sdf.setTimeZone(timeZone);
        try {
            return sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Method to extract time component
    private static Date extractTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            return timeFormat.parse(timeFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean checkClash(Date start1, Date end1, Date start2, Date end2) {
        return start1.before(end2) && end1.after(start2);
    }

    @GetMapping("/getAllCourses")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<Courses> getAllCourses() {
        // setAuthenticatedUserEmail();
        // if (email != null) {
        //     // System.out.println(email);
        //     User user = userService.findByEmail(email).orElse(null);
      
        // }
        List<Courses> courses =  courseService.getCourses();
        courses.sort(Comparator.comparing(Courses::getCRN));
        return courses;
    }

    @GetMapping("/userCourses")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getCourse() {
        if (email == null) {
            setAuthenticatedUserEmail();
            if (email == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not logged in"));
            }
        }

        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
        }
        List<Courses> courses = new java.util.ArrayList<>(
                user.getCourses().stream().map(course -> courseService.getCourseByCRN(course.getCRN())).toList());

        courses.sort(Comparator.comparing(Courses::getCRN));
        return ResponseEntity.ok(courses);

    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> registerCourse(@Valid @RequestBody CourseRequest registerCourseRequest) {
        if (email == null) {
            setAuthenticatedUserEmail();
            if (email == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not logged in"));
            }
        }
        if (registerCourseRequest.getCourseCRNS().length == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No courses to register"));
        }
        for (String CRN : registerCourseRequest.getCourseCRNS()) {
            Courses course = courseService.getCourseByCRN(Integer.parseInt(CRN));
            if (course == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course " + CRN + " not found"));
            }
            if (course.getSeats() == 0) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course " + CRN + " is full"));
                
            }
            User user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
            }
            
            if (user.getCourses().stream().anyMatch(c -> c.getCRN().equals(Integer.parseInt(CRN)))) {
                return ResponseEntity.badRequest()
                .body(new MessageResponse("Error: Course " + CRN + " already registered"));
            }
            for (Courses c : user.getCourses()) {
                if (c.getClassTiming().getday().equalsIgnoreCase(course.getClassTiming().getday()) && checkClash(parseMongoDbTime(c.getClassTiming().getStartTime()),
                        parseMongoDbTime(c.getClassTiming().getEndTime()),
                        parseMongoDbTime(course.getClassTiming().getStartTime()),
                        parseMongoDbTime(course.getClassTiming().getEndTime()))) {
                    return ResponseEntity.badRequest()
                            .body(new MessageResponse("Error: Course " + CRN + " clashes with course " + c.getCRN()));
                }
            }
            Integer total_credits = user.getCourses().stream()
                .mapToInt(Courses::getHours)
                .sum();
            if(total_credits + course.getHours() > 12){
                return ResponseEntity.badRequest()
                            .body(new MessageResponse("Error: Course " + CRN + " exceeds credit limit"));
            }

            user.getCourses().add(course);
            userService.save(user);
            course.setSeats(course.getSeats() - 1);
            course.setEnrollment(course.getEnrollment() + 1);
            courseService.addCourse(course);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course registered successfully"));
    }

    @PostMapping("/drop")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> dropCourse(@Valid @RequestBody CourseRequest dropCourseRequest) {
        if (email == null) {
            setAuthenticatedUserEmail();
            if (email == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not logged in"));
            }
        }
        if (dropCourseRequest.getCourseCRNS().length == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No courses to drop"));
        }
        for (String CRN : dropCourseRequest.getCourseCRNS()) {
            Courses course = courseService.getCourseByCRN(Integer.parseInt(CRN));
            if (course == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course " + CRN + " not found"));
            }
            course.setSeats(course.getSeats() + 1);
            course.setEnrollment(course.getEnrollment() - 1);
            courseService.addCourse(course);

            User user = userService.findByEmail(email).orElse(null);
            if (user == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found"));
            }
            if (!user.getCourses().stream().anyMatch(c -> c.getCRN().equals(Integer.parseInt(CRN)))) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Course " + CRN + " not registered"));
            }
            user.getCourses().removeIf(c -> c.getCRN().equals(Integer.parseInt(CRN)));
            userService.save(user);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course dropped successfully"));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> addCourse(@Valid @RequestBody AddCourseRequest courses) {
        for (Courses course : courses.getCourses()) {
            // System.out.println(course.getTitle());
            if (courseService.getCourseByCRN(course.getCRN()) != null) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Course " + course.getCRN() + " already exists"));
            }

            if (course.getSeats() == null) {
                return ResponseEntity.badRequest()
                        .body(new MessageResponse("Error: Course " + course.getCRN() + " has no seats"));
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
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm a");
                Date startTime = inputFormat.parse(course.getClassTiming().getStartTime());
                course.getClassTiming().setStartTime(startTime.toString());
                Date endTime = inputFormat.parse(course.getClassTiming().getEndTime());
                course.getClassTiming().setEndTime(endTime.toString());
            } catch (ParseException e) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Invalid time format"));
            }
            courseService.addCourse(course);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course added successfully"));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> deleteCourse(@Valid @RequestBody CourseRequest deleteCourseRequest) {
        if (deleteCourseRequest.getCourseCRNS().length == 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No courses to delete"));
        }
        for (String CRN : deleteCourseRequest.getCourseCRNS()) {
            Courses course = courseService.getCourseByCRN(Integer.parseInt(CRN));
            if (course == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: Course " + CRN + " not found"));
            }
            courseService.deleteCourse(course);
        }
        return ResponseEntity.ok().body(new MessageResponse("Course deleted successfully"));
    }

}
