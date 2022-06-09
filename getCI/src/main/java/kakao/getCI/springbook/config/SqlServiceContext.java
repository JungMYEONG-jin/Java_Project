package kakao.getCI.springbook.config;

import kakao.getCI.springbook.issuetracker.sqlservice.updatable.EmbeddedSqlRegistry;
import kakao.getCI.springbook.user.dao.UserDao;
import kakao.getCI.springbook.user.sqlservice.SqlRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import kakao.getCI.springbook.user.sqlservice.OxmSqlService;
import kakao.getCI.springbook.user.sqlservice.SqlService;

import javax.sql.DataSource;

@Configuration
public class SqlServiceContext {

    @Autowired SqlMapConfig sqlMapConfig;

    @Bean
    public SqlService sqlService(){
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setSqlRegistry(sqlRegistry());
        sqlService.setUnmarshaller(unmarshaller());

        sqlService.setSqlmap(this.sqlMapConfig.getSqlMapResource());

        return sqlService;
    }

    @Bean
    public DataSource embeddedDatabase(){
        return new EmbeddedDatabaseBuilder().setName("embeddedDatabase").setType(EmbeddedDatabaseType.H2).addScript("schema.sql").build();
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
        marshaller.setContextPath("kakao.getCI.springbook.user.sqlservice.jaxb");
        return marshaller;
    }
}
