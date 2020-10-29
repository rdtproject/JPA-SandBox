package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.entity.Passport;
import com.tpolm.jpasandpit.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

import static com.tpolm.jpasandpit.entity.EntityConstans.GET_ALL_STUDENTS;

/**
 * Native queries do not involve persistence context, so good to execute refresh() to get the latest changes from db
 */

@Repository
@Transactional
public class StudentRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(StudentRepository.class);

    /* It is just an interface. Implementation is a Persistence Context. */
    @Autowired
    private EntityManager em;

    public List<Student> getAllStudents() {
        Query query = em.createNamedQuery(GET_ALL_STUDENTS, Student.class);
        return query.getResultList();
    }

    public List<Student> getAllStudentsJpql() {
        Query query = em.createQuery("select c from Student c", Student.class);
        return query.getResultList();
    }

    public Student findById(Long id) {
        return em.find(Student.class, id);
    }

    public void deleteById(Long id) {
        Student Student = findById(id);
        em.remove(Student);
    }

    public Student save(Student Student) {
        if (Student.getId() == null) {
            em.persist(Student);
        } else {
            em.merge(Student);
        }
        return Student;
    }

    public void saveStudentWithPassport() {
        Passport passport = new Passport("J123849");
        em.persist(passport);

        Student student = new Student("Janusz");
        student.setPassport(passport);

        em.persist(student);
    }

}
