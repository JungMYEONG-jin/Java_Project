package kakao.getCI.springbook.config;

import kakao.getCI.springbook.user.service.DummyMailSender;
import kakao.getCI.springbook.user.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import kakao.getCI.springbook.user.service.TestUserService;

@Configuration
@Profile("test")
public class TestAppContext {

    @Bean
    public UserService testUserService(){
        return new TestUserService();
    }

    @Bean
    public MailSender mailSender(){
        return new DummyMailSender();
    }
}
