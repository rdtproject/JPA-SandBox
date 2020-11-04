package com.tpolm.jpasandpit;

import com.tpolm.jpasandpit.entity.*;
import com.tpolm.jpasandpit.repository.CourseRepository;
import com.tpolm.jpasandpit.repository.EmployeeRepository;
import com.tpolm.jpasandpit.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JpaSandpitApplication implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CourseRepository courseRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

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

		employeeRepository.insert(new PartTimeEmployee("Jack",  new BigDecimal("50")));
		employeeRepository.insert(new FullTimeEmployee("Srack",  new BigDecimal("10000")));

		logger.info("Employees -> {}", employeeRepository.getEmployees());
	}
}
