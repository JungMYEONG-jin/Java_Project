package springbook.user.sqlservice;

import org.springframework.core.io.ClassPathResource;
import springbook.exception.SqlRetrievalFailureException;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class XmlSqlService implements SqlService{

    private Map<String, String> sqlMap = new HashMap<>();

    public XmlSqlService() throws Exception{
        String contextPath = Sqlmap.class.getPackage().getName();
        System.out.println("contextPath = " + contextPath);
        try{
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Source xmlSource = new StreamSource(new ClassPathResource("../resources/userdao/sqlmap.xml").getInputStream());
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

            for(SqlType sql : sqlmap.getSql()){

                System.out.println("sql.getKey() = " + sql.getKey());
                System.out.println("sql.getValue() = " + sql.getValue());
                sqlMap.put(sql.getKey(), sql.getValue().toString());
            }
        }catch (JAXBException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        String sql = sqlMap.get(key);
        if(sql==null) {
            throw new SqlRetrievalFailureException(key + "를 이용해서 SQL을 찾을 수 없습니다.");
        }else
        {
            return sql;
        }
    }

}
