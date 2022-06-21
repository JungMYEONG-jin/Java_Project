package kakao.getCI;

import kakao.getCI.aop.AopConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@Import(AopConfig.class)
@SpringBootApplication
public class GetCiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetCiApplication.class, args);
	}

}
