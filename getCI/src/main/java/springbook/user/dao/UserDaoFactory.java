package springbook.user.dao;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration // 애플리케이션 컨텍스트 또는 빈팩토리가 사용할 설정정보라는 표시
public class UserDaoFactory {

    @Bean // 오브젝트 생성을 담당하는 IoC용 메소드라는 표시
    // 빈 이름은 userDao가 됨
    public UserDao userDao()
    {
        UserDao userDao = new UserDaoJdbc();
//        userDao.setDataSource(dataSource());
        return userDao;
    }

//    @Bean
//    public ConnectionMaker connectionMaker()
//    {
//        return new NConnectionMaker();
//    }


    @Bean
    public DataSource dataSource()
    {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        return dataSource;
    }


}
