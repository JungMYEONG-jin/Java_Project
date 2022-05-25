package learningtest.spring.oxm;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import springbook.user.sqlservice.jaxb.SqlType;
import springbook.user.sqlservice.jaxb.Sqlmap;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = Runnable.class)
public class OxmTest {

    @Autowired
    Unmarshaller unmarshaller;

    @Test
    void sqlMapTest() throws IOException {

        Source xmlSource = new StreamSource(new ClassPathResource("../resources/userdao/sqlmap.xml").getInputStream());
        Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(xmlSource);
        List<SqlType> sqlTypes = sqlmap.getSql();


    }

}
