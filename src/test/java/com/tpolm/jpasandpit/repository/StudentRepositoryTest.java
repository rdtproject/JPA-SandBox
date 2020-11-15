package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.JpaSandpitApplication;
import com.tpolm.jpasandpit.entity.Address;
import com.tpolm.jpasandpit.entity.Course;
import com.tpolm.jpasandpit.entity.Passport;
import com.tpolm.jpasandpit.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSandpitApplication.class)
public class StudentRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StudentRepository repository;

    @Autowired
    EntityManager em;

    @Test
    @Transactional
    public void retrieveStudentAndPassword() {
        repository.saveStudentWithPassport();

        Student student = repository.findById(2001L);
        assertNotNull(student.getId());
        assertNotNull(student.getName());

        Passport passport = student.getPassport();
        assertNotNull(passport.getId());
        assertNotNull(passport.getNumber());
    }

    @Test
    @Transactional
    public void retrievePassportAndStudent() {
        Passport passport = em.find(Passport.class, 4001L);
        assertNotNull(passport.getId());
        assertNotNull(passport.getNumber());

        Student student = passport.getStudent();
        assertNotNull(student.getId());
        assertNotNull(student.getName());
    }

    @Test
    @Transactional
    public void retrieveStudentAndCourses() {
        Student student = repository.findById(2001L);
        logger.info("Student -> {}", student);
        logger.info("Student courses - {}", student.getCourses());
        assertNotNull(student);
        assertNotNull(student.getCourses());
    }

    @Test
    @Transactional
    public void insertStudentAndCourseTest() {
        Student student = repository.insertStudentAndCOurse(new Student("Jack"),
                new Course("Microservices in no steps"));
        assertNotNull(student);
        assertNotNull(student.getCourses());
        assertTrue(student.getCourses().size() == 1);
    }

    @Test
    @Transactional
    public void setAddressDetails() {
        Student student = em.find(Student.class, 2001L);
        student.setAddress(new Address("Badennerstrasse", "18k", "Geneva"));
        em.flush();
        logger.info("Student: {}", student);
        logger.info("Address: {}", student.getAddress());
    }
}