package com.tpolm.jpasandpit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tpolm.jpasandpit.entity.EntityConstans.GET_ALL_COURSES;
import static com.tpolm.jpasandpit.entity.EntityConstans.GET_ALL_COURSES_FETCH;

@Entity
@NamedQueries({
        @NamedQuery(name = GET_ALL_COURSES, query = "select c from Course c"),
        @NamedQuery(name = GET_ALL_COURSES_FETCH, query = "select c from Course c JOIN FETCH c.students")
})
@Cacheable
@SQLDelete(sql = "update Course set is_deleted=true where id=?")
@Where(clause = "is_deleted = false")
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "courses")
    @JsonIgnore
    private List<Student> students = new ArrayList<>();

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime lastUpdateDate;

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime creationDate;

    private boolean isDeleted;

    @PreRemove
    private void preRemove() {
         this.isDeleted = true;
    }

    public Course() {
    }

    public Course(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void deleteReview(Review review) {
        this.reviews.remove(review);
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastUpdateDate=" + lastUpdateDate +
                ", creationDate=" + creationDate +
                '}';
    }
}
