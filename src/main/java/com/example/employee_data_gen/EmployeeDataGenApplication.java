package com.example.employee_data_gen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EmployeeDataGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeDataGenApplication.class, args);
	}

}
