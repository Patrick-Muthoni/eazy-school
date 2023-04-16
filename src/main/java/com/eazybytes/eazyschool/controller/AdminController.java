package com.eazybytes.eazyschool.controller;

import com.eazybytes.eazyschool.model.Course;
import com.eazybytes.eazyschool.model.EazyClass;
import com.eazybytes.eazyschool.model.Person;
import com.eazybytes.eazyschool.repository.CoursesRepository;
import com.eazybytes.eazyschool.repository.EazyClassRepository;
import com.eazybytes.eazyschool.repository.PersonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin")
public class AdminController {

    private final EazyClassRepository eazyClassRepository;
    private final PersonRepository personRepository;
    private final CoursesRepository coursesRepository;

    public AdminController(EazyClassRepository eazyClassRepository, PersonRepository personRepository, CoursesRepository coursesRepository) {
        this.eazyClassRepository = eazyClassRepository;
        this.personRepository = personRepository;
        this.coursesRepository = coursesRepository;
    }

    @GetMapping("/displayClasses")
    public ModelAndView displayClasses (Model model) {
        List<EazyClass> eazyClasses = eazyClassRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("classes.html");
        modelAndView.addObject("eazyClasses", eazyClasses);
        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass (Model model, @ModelAttribute("eazyClass") EazyClass eazyClass) {
        eazyClassRepository.save(eazyClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        modelAndView.addObject("eazyClass", new EazyClass());
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass (Model model, @RequestParam int id) {
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(id);
        if (eazyClass.isPresent()) {
            for (Person person : eazyClass.get().getPersons()) {
                person.setEazyClass(null);
                personRepository.save(person);
            }
            eazyClassRepository.deleteById(id);
        }
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model, @RequestParam int classId, HttpSession session,
                                        @RequestParam(value = "error", required = false) String error) {
        String errorMsg = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<EazyClass> eazyClass = eazyClassRepository.findById(classId);
        //add is present check
        modelAndView.addObject("eazyClass", eazyClass.get());
        modelAndView.addObject("person", new Person());
        session.setAttribute("eazyClass", eazyClass.get());

        if (error != null) {
            errorMsg = "Invalid Email Address entered";
            modelAndView.addObject("errorMessage", errorMsg);
        }

        return modelAndView;
    }

    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model, @ModelAttribute("person") Person student, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        //get the class we are on from session
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        //validate we have this email as person in db, remember the action here is updating details to assign a class
        Person personStudent = personRepository.readByEmail(student.getEmail());
        if (personStudent == null) {
            modelAndView.setViewName("redirect:/admin/displayStudents?classId=" + eazyClass.getClassId() + "&error=true");
            return modelAndView;
        }
        personStudent.setEazyClass(eazyClass);
        personRepository.save(personStudent);
        //eazyClass.getPersons().add(personStudent);
        //eazyClassRepository.save(eazyClass);
        modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId=" + eazyClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model, @RequestParam int personId, HttpSession session) {
        //Remember to add isPresent checks
        EazyClass eazyClass = (EazyClass) session.getAttribute("eazyClass");
        Optional<Person> person = personRepository.findById(personId);
        //remove class link from the person object
        person.get().setEazyClass(null);
        //?no update to personRepository?
        //remove person from eazy class
        eazyClass.getPersons().remove(person.get());
        //save the change
        EazyClass newEazyClassState = eazyClassRepository.save(eazyClass);
        //update session
        session.setAttribute("eazyClass", newEazyClassState);
        return new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model) {
//        List<Course> cours = coursesRepository.findByOrderByName();
        List<Course> cours = coursesRepository.findAll(Sort.by("name").descending());
        ModelAndView modelAndView = new ModelAndView("courses_secure.html");

        modelAndView.addObject("courses", cours);
        //below required to bring course information from the ui
        model.addAttribute("course", new Course());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addCourse(Model model, @ModelAttribute("course") Course course) {
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model, @RequestParam int id , HttpSession session,
                                     @RequestParam(required = false) String error) {
        String errorMsg = null;
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        Optional<Course> course = coursesRepository.findById(id);
        modelAndView.addObject("courses", course.get());
        modelAndView.addObject("person", new Person());
        session.setAttribute("courses", course.get());
        if (error != null) {
            errorMsg = "Invalid Email Address entered!";
            modelAndView.addObject("errorMessage", errorMsg);
        }
        return modelAndView;
    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse (Model model, @ModelAttribute("person") Person person, HttpSession session) {

        ModelAndView modelAndView = new ModelAndView();
        Course course = (Course) session.getAttribute("course");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if (personEntity == null) {
            modelAndView.setViewName("redirect:/admin/viewStudents?id"+course.getCourseId()+"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(course);
        course.getPersons().add(personEntity);
        personRepository.save(personEntity);
        //update session to always have the latest course object
        session.setAttribute("courses", course);
        modelAndView.setViewName("redirect:/admin/viewStudents?id"+course.getCourseId());
        return modelAndView;

    }

    @RequestMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse (Model model, @RequestParam int personId, HttpSession session) {
        //from what course are we deleting the person
        Course course = (Course) session.getAttribute("course");
        //get the person
        Optional<Person> person = personRepository.findById(personId);
        person.get().getCourses().remove(course);
        course.getPersons().remove(person.get());
        personRepository.save(person.get());
        session.setAttribute("courses", course);
        return new ModelAndView("redirect:/admin/viewStudents?id"+course.getCourseId());
    }


}
