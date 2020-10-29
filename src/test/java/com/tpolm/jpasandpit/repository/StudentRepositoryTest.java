package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.JpaSandpitApplication;
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

import static org.junit.jupiter.api.Assertions.*;

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

}