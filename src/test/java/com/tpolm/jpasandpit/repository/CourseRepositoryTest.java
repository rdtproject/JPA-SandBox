package com.tpolm.jpasandpit.repository;


import com.tpolm.jpasandpit.JpaSandpitApplication;
import com.tpolm.jpasandpit.entity.Course;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSandpitApplication.class)
public class CourseRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CourseRepository repository;

    @Test
    @Ignore
    public void findById() {
        Course course = repository.findById(10001L);
        assertEquals("JPA in 50 steps", course.getName());
    }

    @Test
    @DirtiesContext
    @Ignore
    public void deleteById() {
        assertNotNull(repository.findById(10002L));
        repository.deleteById(10002L);
        assertNull(repository.findById(10002L));
    }

    @Test
    @Ignore
    public void save_insert() {
        Course course = repository.save(new Course("Karate"));
        assertNotNull(course);
        assertNotNull(course.getId());
    }

    @Test
    @Ignore
    public void save_update() {
        Course course = repository.findById(10001L);
        assertEquals("JPA in 50 steps", course.getName());
        course.setName("AWS");
        repository.save(course);
        course = repository.findById(10001L);
        assertEquals("AWS", course.getName());
    }


    @Test
    public void playWIthEntityManager() {
        repository.playWIthEntityManager();
    }
}