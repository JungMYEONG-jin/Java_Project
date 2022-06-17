package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import study.datajpa.config.AppV1Config;
import study.datajpa.config.AppV2Config;
import study.datajpa.config.v1_proxy.ConcreteProxyConfig;
import study.datajpa.config.v1_proxy.InterfaceProxyConfig;
import study.datajpa.config.v2_dynamicproxy.DynamicProxyBasicConfig;
import study.datajpa.config.v2_dynamicproxy.DynamicProxyFilterConfig;
import study.datajpa.config.v6_aop.AopConfig;
import study.datajpa.trace.logtrace.LogTrace;
import study.datajpa.trace.logtrace.ThreadLocalLogTrace;

import java.util.Optional;
import java.util.UUID;

//@Import({AppV1Config.class, AppV2Config.class}) // 해당 클래스를 스프링 빈으로 등록
@EnableJpaAuditing // 추적
//@Import(InterfaceProxyConfig.class)
//@Import(ConcreteProxyConfig.class)
//@Import(DynamicProxyBasicConfig.class)
//@Import(DynamicProxyFilterConfig.class)
@Import(AopConfig.class)
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

	@Bean
	public LogTrace logTrace(){
		return new ThreadLocalLogTrace();
	}

}
