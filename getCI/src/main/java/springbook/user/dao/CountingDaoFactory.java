package springbook.user.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDao userDao()
    {
        UserDao userDao = new UserDaoJdbc();
//        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker()
    {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker()
    {
        return new NConnectionMaker();
    }

    @Bean
    public DataSource dataSource()
    {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        return dataSource;
    }

}
