package kakao.getCI.springbook.config;

import kakao.getCI.springbook.user.service.DummyMailSender;
import kakao.getCI.springbook.user.service.UserServiceTest.TestUserService;
import kakao.getCI.springbook.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
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
@PropertySource("/application.properties")
//@ImportResource("/applicationContext.xml")
public class AppContext {

    @Autowired
    Environment env;

    @Value("${spring.datasource.driver-class-name}") Class<? extends Driver> driverClass;
    @Value("${spring.datasource.url}") String url;
    @Value("${spring.datasource.username}") String username;
    @Value("${spring.datasource.password}") String password;

    // 반드시 static 선언
    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
        return new PropertySourcesPlaceholderConfigurer();
    }



    @Bean
    public DataSource dataSource(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriverClass(Driver.class);
//        dataSource.setUrl("jdbc:h2:tcp://localhost/~/tobey");
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        try{
//            dataSource.setDriverClass((Class<? extends java.sql.Driver>)Class.forName(env.getProperty("db.driverClass")));
//        }catch (ClassNotFoundException e){
//            throw new RuntimeException(e);
//        }
//
//        dataSource.setUrl("jdbc:h2:tcp://localhost/~/tobey");
//      dataSource.setUsername("sa");
//      dataSource.setPassword("");

        dataSource.setDriverClass(this.driverClass);
        dataSource.setUrl(this.url);
        dataSource.setUsername(this.username);
        dataSource.setPassword(this.password);

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
