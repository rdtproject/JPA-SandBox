package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.entity.Course;
import com.tpolm.jpasandpit.entity.Review;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static com.tpolm.jpasandpit.entity.EntityConstans.GET_ALL_COURSES;

/**
 * Native queries do not involve persistence context, so good to execute refresh() to get the latest changes from db
 */

@Repository
@Transactional
public class CourseRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseRepository.class);

    /* It is just an interface. Implementation is a Persistence Context. */
    @Autowired
    private EntityManager em;

    public List<Course> getAllCourses() {
        Query query = em.createNamedQuery(GET_ALL_COURSES, Course.class);
        return query.getResultList();
    }

    public List<Course> getAllCoursesJpql() {
        Query query = em.createQuery("select c from Course c", Course.class);
        return query.getResultList();
    }

    public List<Course> getAllCoursesNative() {
        Query query = em.createNativeQuery("select * from Course", Course.class);
        return query.getResultList();
    }

    public List<Course> getAllCoursesIn12StepsNative() {
        Query query = em.createNativeQuery("select * from Course c where c.name like ?", Course.class);
        query.setParameter(1, "%12 steps");
        return query.getResultList();
    }

    /*
        Mass update via JPA would traverse through all Course rows - performance issue.
        Terrible performance, native sql query seems to be a much better solution.
     */
    public int updateModificationDateForAllRowsNative(LocalDateTime dateTime) {
        Query query = em.createNativeQuery("update Course c set c.updated = ?");
        query.setParameter(1, dateTime);
        return query.executeUpdate();
    }

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

        Course course2 = new Course("RESTful Services");
        em.persist(course2);

        em.flush();

        course1.setName("Web services - updated");
        course2.setName("RESTful Services - updated");

        em.refresh(course1);

        LOGGER.info("playWIthEntityManager - stop");
    }

    public void addReviewsForCourse(Long courseId, List<Review> reviews) {
        Course course = findById(courseId);

        // Review is owning side of the relationship!
        // so each review needs to have a proper reference to a course
        for (Review review : reviews) {
            course.addReview(review);
            review.setCourse(course); // as owning part of the relation needs to know course
            em.persist(review);
        }
    }
}
