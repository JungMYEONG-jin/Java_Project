package kakao.getCI;

import kakao.getCI.com.aop.SimpleLogAop;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Import(SimpleLogAop.class)
@SpringBootApplication
public class GetCiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetCiApplication.class, args);
	}

}
