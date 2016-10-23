package ru.mail.park.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.mail.park.Application;

@SpringBootApplication
@Import(Application.class)
public class JavaHerokuPostgresTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaHerokuPostgresTestApplication.class, args);
	}
}
