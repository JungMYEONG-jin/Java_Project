package com.simpleauthJPA;

import com.simpleauthJPA.aop.SALogAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(SALogAspect.class)
@SpringBootApplication
public class SimpleauthJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleauthJpaApplication.class, args);
	}

}
