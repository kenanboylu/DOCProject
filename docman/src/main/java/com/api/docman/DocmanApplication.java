package com.api.docman;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class DocmanApplication {
	
	/*
	 * @author kenanboylu 27/11/2021
	 * Document Management Rest Api
	 */

	public static void main(String[] args) {
		SpringApplication.run(DocmanApplication.class, args);
	}

}
