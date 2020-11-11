package com.tpolm.jpasandpit.repository;


import com.tpolm.jpasandpit.JpaSandpitApplication;
import com.tpolm.jpasandpit.entity.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSandpitApplication.class)
public class CourseRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourseRepository courseRepo;

    @Test
    public void getAllCourses() {
        List<Course> courses = courseRepo.getAllCourses();
        assertTrue(courses.size() > 2);
    }

    @Test
    public void getAllCoursesJpql() {
        List<Course> courses = courseRepo.getAllCoursesJpql();
        assertTrue(courses.size() > 2);
    }

    @Test
    public void getAllCoursesNative() {
        List<Course> courses = courseRepo.getAllCoursesNative();
        assertTrue(courses.size() > 2);
    }

    @Test
    public void getAllCoursesIn12StepsNative() {
        List<Course> courses = courseRepo.getAllCoursesIn12StepsNative();
        assertTrue(courses.size() == 1);
    }

    @Test
    public void updateModificationDateForAllRowsNative() {
        LocalDateTime dateTime = LocalDateTime.now();
        int result = courseRepo.updateModificationDateForAllRowsNative(dateTime);
        List<Course> courses = courseRepo.getAllCourses();
        assertEquals(dateTime.toLocalDate(), courses.iterator().next().getLastUpdateDate().toLocalDate());
        assertTrue(result > 0);
    }

    @Test
    public void findById() {
        Course course = courseRepo.findById(10001L);
        assertEquals("JPA in 50 steps", course.getName());
    }

    @Test
    @DirtiesContext
    @Transactional
    public void deleteById() {
        Course course = courseRepo.findById(10002L);
        assertNotNull(course);

        courseRepo.deleteById(10002L);
        assertNull(courseRepo.findById(10002L));

    }

    @Test
    public void save_insert() {
        Course course = courseRepo.save(new Course("Karate"));
        assertNotNull(course);
        assertNotNull(course.getId());
    }

    @Test
    public void save_update() {
        Course course = courseRepo.findById(10001L);
        assertEquals("JPA in 50 steps", course.getName());
        course.setName("AWS");
        courseRepo.save(course);
        course = courseRepo.findById(10001L);
        assertEquals("AWS", course.getName());
    }

    @Test
    public void playWIthEntityManager() {
        courseRepo.playWIthEntityManager();
    }
}