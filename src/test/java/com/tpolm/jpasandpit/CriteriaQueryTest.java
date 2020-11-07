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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class CriteriaQueryTest {

    Logger logger = LoggerFactory.getLogger(CriteriaQueryTest.class);

    @Autowired
    EntityManager em;

    @Test
    void jpql_basics() {
//        TypedQuery<Course> query = em.createQuery("select c from Course c", Course.class);
//        List resultList = query.getResultList();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Course> cq = cb.createQuery(Course.class);
        Root<Course> courseRoot = cq.from(Course.class);
        TypedQuery<Course> query = em.createQuery(cq.select(courseRoot));
        List resultList = query.getResultList();
        logger.info("Courses -> {}", resultList);
    }

}
