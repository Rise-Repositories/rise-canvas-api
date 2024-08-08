package school.sptech.crudrisecanvas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrudRiseCanvasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudRiseCanvasApplication.class, args);
	}

}
