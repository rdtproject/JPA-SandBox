package com.tpolm.jpasandpit.repository;


import com.tpolm.jpasandpit.JpaSandpitApplication;
import com.tpolm.jpasandpit.entity.Course;
import com.tpolm.jpasandpit.entity.EntityConstans;
import com.tpolm.jpasandpit.entity.ReviewRating;
import com.tpolm.jpasandpit.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaSandpitApplication.class)
public class PerformanceTuningTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EntityManager em;

    @Test
    @Transactional
    public void getAllCoursesNPlusOneProblem() {
        List<Course> courses = em.createNamedQuery(EntityConstans.GET_ALL_COURSES, Course.class)
                .getResultList();

        for (Course course : courses) {
            logger.info("Course => {}, Students => {}", course, course.getStudents());
        }
    }

    @Test
    @Transactional
    public void getAllCoursesNPlusOneProblemFetchJoin() {
        List<Course> courses = em.createNamedQuery(EntityConstans.GET_ALL_COURSES_FETCH, Course.class)
                .getResultList();

        for (Course course : courses) {
            logger.info("Course => {}, Students => {}", course, course.getStudents());
        }
    }

    @Test
    @Transactional
    public void getAllCoursesNPlusOneProblemEntityGraph() {
        EntityGraph<Course> entityGraph = em.createEntityGraph(Course.class);
        entityGraph.addSubgraph("students");

        List<Course> courses = em.createNamedQuery(EntityConstans.GET_ALL_COURSES, Course.class)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();

        for (Course course : courses) {
            logger.info("Course => {}, Students => {}", course, course.getStudents());
        }
    }
}