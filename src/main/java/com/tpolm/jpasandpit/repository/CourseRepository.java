package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.entity.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CourseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRepository.class);

    @Autowired
    EntityManager em;

    public Course findById(Long id) {
        return em.find(Course.class, id);
    }

    public void deleteById(Long id) {
        Course course = findById(id);
        em.remove(course);
    }

    public Course save(Course course) {
        if (course.getId() == null) {
            em.persist(course);
        } else {
            em.merge(course);
        }
        return course;
    }

    public void playWIthEntityManager() {
        LOGGER.info("playWIthEntityManager - start");

        Course course1 = new Course("Web Services");
        em.persist(course1);
        course1.setName("Web services - updated");

        Course course2 = new Course("RESTful Services");
        em.persist(course2);

        em.flush();
        em.detach(course2);

        course2.setName("RESTful Services - updated");

        LOGGER.info("playWIthEntityManager - stop");
    }
}
