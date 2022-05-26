package springbook.user.sqlservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import springbook.exception.SqlNotFoundException;
import springbook.exception.SqlRetrievalFailureException;
import springbook.user.dao.UserDao;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService, SqlRegistry, SqlReader{

    private Map<String, String> sqlMap = new HashMap<>();
    // 이름을 값으로 넣는건 좋지 않은 습관임. set 하게끔 변경
    private Resource sqlmap = new ClassPathResource("sqlmap.xml", UserDao.class);
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    public void setsqlmap(Resource sqlmap) {
        this.sqlmap = sqlmap;
    }

    @PostConstruct
    public void loadSql()
    {
        this.sqlReader.read(this.sqlRegistry);
    }

    public XmlSqlService(){
    }

    public String getSql(String key) throws SqlRetrievalFailureException {
        try{
            return this.sqlRegistry.findSql(key);
        }catch (SqlNotFoundException e){
            throw new SqlRetrievalFailureException(e);
        }
    }

    // object to xml is marshaller
    // xml to object is unmarshaller


    @Override
    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try{
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Source xmlSource = new StreamSource(this.sqlmap.getInputStream());
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

            for(SqlType sql : sqlmap.getSql()){
               sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }
        }catch (JAXBException e){
            throw new RuntimeException(e);
        }catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if(sql == null){
            throw new SqlNotFoundException(key+" 에 대한 SQL을 찾을 수 없습니다.");
        }else
            return sql;
    }
}
