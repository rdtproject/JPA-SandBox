package com.tpolm.jpasandpit.repository;


import com.tpolm.jpasandpit.JpaSandpitApplication;
import com.tpolm.jpasandpit.entity.Course;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSandpitApplication.class)
public class CourseSpringDataRepositoryTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CourseSpringDataRepository repository;

    @Test
    public void findByIdOk() {
        Optional<Course> courseOptional = repository.findById(10001L);
        assertTrue(courseOptional.isPresent());
    }

    @Test
    public void findByIdNotOk() {
        Optional<Course> courseOptional = repository.findById(100011111L);
        assertTrue(courseOptional.isEmpty());
    }

    @Test
    public void playWithSpringDataRepo_Crud() {
        Course course = new Course("Microservices in 200 steps");
        repository.save(course);
        course.setName("Ala ma kota");
        repository.save(course);
    }

    @Test
    public void playWithSpringDataRepo_Sort() {
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        logger.info("{}", repository.findAll(sort));
    }

    @Test
    public void playWithSpringDataRepo_Count() {
        logger.info("{}", repository.count());
    }

    @Test
    public void playWithSpringDataRepo_Custom_Queries() {
        logger.info("Find by name: {}", repository.findByName("JPA in 50 steps"));
    }

    @Test
    public void playWithSpringDataRepo_Native_Queries() {
        logger.info("Find by name: {}", repository.getAllCoursesWith50InNameNative());
    }

    @Test
    public void playWithSpringDataRepo_Named_Queries() {
        logger.info("Find all by named query: {}", repository.getAllCoursesNamedQuery());
    }

}