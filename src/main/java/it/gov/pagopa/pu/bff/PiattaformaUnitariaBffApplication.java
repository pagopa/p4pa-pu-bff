package it.gov.pagopa.pu.bff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class, ErrorMvcAutoConfiguration.class})
public class PiattaformaUnitariaBffApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiattaformaUnitariaBffApplication.class, args);
	}

}
