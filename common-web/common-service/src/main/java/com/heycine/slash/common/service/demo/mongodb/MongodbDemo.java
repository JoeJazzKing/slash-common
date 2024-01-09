package com.heycine.slash.common.service.demo.mongodb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class MongodbDemo {

	@Autowired(required = false)
	MongoTemplate mongoTemplate;

	@PostMapping("/mongo/insert")
	public String send() {
		Student student = new Student();
		student.setId(1L);
		student.setUsername("我是小周周");
		student.setTimer(LocalDateTime.now());
		mongoTemplate.insert(student);
		return "SUCESS";
	}

	@GetMapping("/mongo/find/{id}")
	public Student findOne(@PathVariable("id") Long id) {
		Student student = mongoTemplate.findOne(
				new Query(Criteria.where("_id").is(id)),
				Student.class
		);
		return student;
	}
}
