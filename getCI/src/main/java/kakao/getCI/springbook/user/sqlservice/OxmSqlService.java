package kakao.getCI.springbook.user.sqlservice;

import kakao.getCI.springbook.exception.SqlRetrievalFailureException;
import kakao.getCI.springbook.user.dao.UserDao;
import kakao.getCI.springbook.user.sqlservice.jaxb.SqlType;
import kakao.getCI.springbook.user.sqlservice.jaxb.Sqlmap;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService{

    private final BaseSqlService baseSqlService = new BaseSqlService();


    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry){
        this.sqlRegistry = sqlRegistry;
    }

    private class OxmSqlReader implements SqlReader{

        private Unmarshaller unmarshaller;

        private final String DEAFULT_FILE = "/userdao/sqlmap.xml";
        private Resource sqlmap = new ClassPathResource("sqlmap.xml", UserDao.class);

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmap(Resource sqlmap) {
            this.sqlmap = sqlmap;
        }

        @Override
        public void read(SqlRegistry sqlRegistry) {

            try{
                Source xmlSource = new StreamSource(sqlmap.getInputStream());
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

                for(SqlType sql : sqlmap.getSql()){
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }


            }catch (IOException e){
                throw new IllegalArgumentException(this.sqlmap.getFilename()+"을 가져올 수 없습니다.", e);
            }
        }

        public Resource getSqlmap() {
            return sqlmap;
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        return this.baseSqlService.getSql(key);
    }

    public void setUnmarshaller(Unmarshaller unmarshaller){
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }


    @PostConstruct
    public void loadSql(){
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);

        this.baseSqlService.loadSql();
    }




}
