package cn.corner.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The class Paas cloud gateway application.
 *
 * @author paascloud.net@gmail.com
 */
@SpringBootApplication
@RestController
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(){
		return "test.....";
	}

}
