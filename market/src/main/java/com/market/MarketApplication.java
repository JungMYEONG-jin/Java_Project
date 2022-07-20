package com.market;

import com.market.daemon.DaemonStarter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
public class MarketApplication {

	@Autowired
	DaemonStarter daemonStarter;

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	@Scheduled(cron = "0 0 6 15 * ?")// 매월 매일 오전 6시 15분
	public void init(){
		daemonStarter.run();
	}

}
