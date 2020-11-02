package com.tpolm.jpasandpit;

import com.tpolm.jpasandpit.entity.Course;
import com.tpolm.jpasandpit.entity.Review;
import com.tpolm.jpasandpit.entity.Student;
import com.tpolm.jpasandpit.repository.CourseRepository;
import com.tpolm.jpasandpit.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JpaSandpitApplication implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	public static void main(String[] args) {
		SpringApplication.run(JpaSandpitApplication.class, args);
	}

	@Override
	@Transactional
	public void run(String... args) throws Exception {
//		List<Review> reviews = new ArrayList<>();
//		reviews.add(new Review("5", "Very gooood"));
//		reviews.add(new Review("1", "Very baaaaaaad"));
//		courseRepository.addReviewsForCourse(10003L, reviews);

		Course course = courseRepository.findById(10002L);
		for (Student student : course.getStudents()) {
			student.getCourses().remove(course);
		}
		course.getStudents().clear();
		courseRepository.deleteById(course.getId());

	}
}
