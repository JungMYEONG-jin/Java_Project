package kakao.getCI.springbook.config;

import kakao.getCI.springbook.user.service.DummyMailSender;
import kakao.getCI.springbook.user.service.UserServiceTest.TestUserService;
import kakao.getCI.springbook.user.service.UserService;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "kakao.getCI")
@Import(SqlServiceContext.class)
//@ImportResource("/applicationContext.xml")
public class AppContext {

    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:h2:tcp://localhost/~/tobey");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }


    @Configuration
    @Profile("production")
    public static class ProductionAppContext{
        @Bean
        public MailSender mailSender(){
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost("127.0.0.1");
            mailSender.setPort(25);
            return mailSender;
        }
    }

    @Configuration
    @Profile("test")
    public static class TestAppContext{
        @Bean
        public UserService testUserService(){
            return new TestUserService();
        }

        @Bean
        public MailSender mailSender(){
            return new DummyMailSender();
        }

    }





}
