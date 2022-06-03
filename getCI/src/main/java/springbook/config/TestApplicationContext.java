package springbook.config;

import com.sun.xml.internal.fastinfoset.tools.FI_DOM_Or_XML_DOM_SAX_SAXEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springbook.issuetracker.sqlservice.updatable.EmbeddedSqlRegistry;
import springbook.user.dao.UserDao;
import springbook.user.dao.UserDaoJdbc;
import springbook.user.service.DummyMailSender;
import springbook.user.service.TestUserService;
import springbook.user.service.UserService;
import springbook.user.service.UserServiceImpl;
import springbook.user.sqlservice.OxmSqlService;
import springbook.user.sqlservice.SqlRegistry;
import springbook.user.sqlservice.SqlService;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
@EnableTransactionManagement
//@ImportResource("/applicationContext.xml")
public class TestApplicationContext {

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

    @Bean
    public UserDao userDao(){
        UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
        userDaoJdbc.setDataSource(dataSource());
        userDaoJdbc.setSqlService(sqlService());
        return userDaoJdbc;
    }

    @Bean
    public UserService userService(){
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setMailSender(mailSender());
        return userService;
    }

    @Bean
    public UserService testUserService(){
        TestUserService testUserService = new TestUserService();
        testUserService.setUserDao(userDao());
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public MailSender mailSender(){
        return new DummyMailSender();
    }

    @Bean
    public SqlService sqlService(){
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.setUnmarshaller(unmarshaller());
        return sqlService;
    }

    @Bean
    public DataSource embeddedDatabase(){
        return new EmbeddedDatabaseBuilder().setName("embeddedDatabase").setType(EmbeddedDatabaseType.H2).addScript("../resources/data.sql").build();
    }


    @Bean
    public SqlRegistry sqlRegistry(){
        EmbeddedSqlRegistry sqlRegistry = new EmbeddedSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller(){
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("springbook.user.sqlservice.jaxb");
        return marshaller;
    }


}
