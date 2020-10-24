package com.tpolm.jpasandpit;

import com.tpolm.jpasandpit.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JpaSandpitApplication implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CourseRepository courseRepository;

	public static void main(String[] args) {
		SpringApplication.run(JpaSandpitApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Course details -> {}", courseRepository.findById(10001L));
		//courseRepository.deleteById(10001L);
	}
}
