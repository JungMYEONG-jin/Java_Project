package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import study.datajpa.config.AppV1Config;
import study.datajpa.config.AppV2Config;

import java.util.Optional;
import java.util.UUID;

@Import({AppV1Config.class, AppV2Config.class}) // 해당 클래스를 스프링 빈으로 등록
@EnableJpaAuditing // 추적
@SpringBootApplication(scanBasePackages = "study.datajpa.proxy.app")
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}


	@Bean
	public AuditorAware<String> auditorProvider()
	{
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
