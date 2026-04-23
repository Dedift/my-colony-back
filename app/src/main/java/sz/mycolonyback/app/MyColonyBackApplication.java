package sz.mycolonyback.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "sz.mycolonyback")
public class MyColonyBackApplication {

	static void main(String[] args) {
		SpringApplication.run(MyColonyBackApplication.class, args);
	}

}
