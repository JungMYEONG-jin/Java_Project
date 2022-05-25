package springbook.user.sqlservice;

import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import springbook.exception.SqlNotFoundException;
import springbook.exception.SqlRetrievalFailureException;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

public class OxmSqlService implements SqlService{

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();

    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setSqlRegistry(SqlRegistry sqlRegistry){
        this.sqlRegistry = sqlRegistry;
    }

    private class OxmSqlReader implements SqlReader{

        private Unmarshaller unmarshaller;

        private final static String DEAFULT_FILE = "../resources/userdao/sqlmap.xml";
        private String sqlMapFile = DEAFULT_FILE;

        public void setUnmarshaller(Unmarshaller unmarshaller) {
            this.unmarshaller = unmarshaller;
        }

        public void setSqlmapFile(String sqlmapFile) {
            this.sqlmapFile = sqlmapFile;
        }

        private String sqlmapFile;

        @Override
        public void read(SqlRegistry sqlRegistry) {
            try{
                Source xmlSource = new StreamSource(new ClassPathResource(this.sqlMapFile).getInputStream());
                Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);

                for(SqlType sql : sqlmap.getSql()){
                    sqlRegistry.registerSql(sql.getKey(), sql.getValue());
                }


            }catch (IOException e){
                throw new IllegalArgumentException(this.sqlMapFile+"을 가져올 수 없습니다.", e);
            }
        }
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try{
            return this.sqlRegistry.findSql(key);
        }catch (SqlNotFoundException e){
            throw new SqlRetrievalFailureException(e);
        }
    }

    public void setUnmarshaller(Unmarshaller unmarshaller){
        this.oxmSqlReader.setUnmarshaller(unmarshaller);
    }

    public void setSqlMapFile(String sqlmapFile){
        this.oxmSqlReader.setSqlmapFile(sqlmapFile);
    }

    @PostConstruct
    public void loadSql(){
        this.oxmSqlReader.read(this.sqlRegistry);
    }
}
