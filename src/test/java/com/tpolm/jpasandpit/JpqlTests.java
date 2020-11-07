package com.tpolm.jpasandpit;

import com.tpolm.jpasandpit.entity.Course;
import com.tpolm.jpasandpit.entity.Student;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class JpqlTests {

    Logger logger = LoggerFactory.getLogger(JpqlTests.class);

    @Autowired
    EntityManager em;

    @Test
    void jpql_courses_without_students() {
        TypedQuery<Course> query = em.createQuery("select c from Course c where c.students is empty", Course.class);
        List<Course> resultList = query.getResultList();

        logger.info("Courses -> {}", resultList);
    }

    @Test
    void jpql_courses_with_at_least_2_students() {
        TypedQuery<Course> query = em.createQuery("select c from Course c where size(c.students) >= 2", Course.class);
        List<Course> resultList = query.getResultList();

        logger.info("Courses -> {}", resultList);
    }

    @Test
    void jpql_courses_order_by_students_amount() {
        TypedQuery<Course> query = em.createQuery("select c from Course c order by size(c.students)", Course.class);
        List<Course> resultList = query.getResultList();

        logger.info("Courses -> {}", resultList);
    }

    @Test
    void jpql_students_with_passport_like() {
        TypedQuery<Student> query = em.createQuery("select s from Student s where s.passport.number like '%1234%'", Student.class);
        List<Student> resultList = query.getResultList();

        logger.info("Students -> {}", resultList);
    }

    @Test
    public void join() {
        Query query = em.createQuery("select c, s from Course c JOIN c.students s");
        List<Object[]> resultList = query.getResultList();

        for (Object[] object : resultList) {
            logger.info("{} {}", object[0], object[1]);
        }
    }

}
