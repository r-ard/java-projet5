package net.safety.alerts.safetynet;

import net.safety.alerts.safetynet.database.JSONDatabase;
import net.safety.alerts.safetynet.exceptions.SafetyException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class SafetynetApplication {

	public static void main(String[] args) throws SafetyException {
		if(args.length == 0) {
			JSONDatabase.initDatabase(new ClassPathResource("database.json").getPath());
		}
		else JSONDatabase.initDatabase(args[0]);

		SpringApplication.run(SafetynetApplication.class, args);
	}
}
