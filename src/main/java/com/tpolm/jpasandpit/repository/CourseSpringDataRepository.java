package com.tpolm.jpasandpit.repository;

import com.tpolm.jpasandpit.entity.Course;
import com.tpolm.jpasandpit.entity.EntityConstans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(path = "courses")
public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {

    List<Course> findByName(String name);

    List<Course> findByNameOrderByIdDesc(String name);

    @Query("select c from Course c")
    List<Course> getAllCourses();

    @Query(value = "select * from Course c where c.name like '%50%' ", nativeQuery = true)
    List<Course> getAllCoursesWith50InNameNative();

    @Query(name = EntityConstans.GET_ALL_COURSES)
    List<Course> getAllCoursesNamedQuery();
}
