package kr.spring.care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableJpaAuditing
//@EnableJpaRepositories(basePackages = "kr.spring.care.member.repository")
public class ElderlyCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElderlyCareApplication.class, args);
	}

}