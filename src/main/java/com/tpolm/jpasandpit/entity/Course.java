package com.tpolm.jpasandpit.entity;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.tpolm.jpasandpit.entity.EntityConstans.GET_ALL_COURSES;

@Entity
@NamedQueries(
        @NamedQuery(name = GET_ALL_COURSES, query = "select c from Course c")
)
public class Course {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime lastUpdateDate;

    @CreationTimestamp
    @Column(name = "created")
    private LocalDateTime creationDate;


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
