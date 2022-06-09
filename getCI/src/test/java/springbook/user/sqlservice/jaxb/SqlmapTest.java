package springbook.user.sqlservice.jaxb;

import kakao.getCI.springbook.user.sqlservice.jaxb.SqlType;
import kakao.getCI.springbook.user.sqlservice.jaxb.Sqlmap;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import java.io.IOException;
import java.util.List;

@SpringBootTest(classes = Runnable.class)
class SqlmapTest {

    @Test
    void getSql() throws JAXBException, IOException {
        System.out.println(" Start jaxb test ");

        String contextPath = Sqlmap.class.getPackage().getName();
        System.out.println("contextPath = " + contextPath);
        JAXBContext context = JAXBContext.newInstance(contextPath);

        Unmarshaller unmarshaller = context.createUnmarshaller();

        Source xmlSource = new StreamSource(new ClassPathResource("sqlmap.xml").getInputStream());
        System.out.println("xmlSource = " + xmlSource.toString());

//        // 테스트 클래스와 같은 폴더내에 있는 xml 사용
        Sqlmap sqlmap = (Sqlmap)unmarshaller.unmarshal(xmlSource);

        List<SqlType> sqlTypeList = sqlmap.getSql();

        Assertions.assertThat(sqlTypeList.size()).isEqualTo(3);
        Assertions.assertThat(sqlTypeList.get(0).getKey()).isEqualTo("add");
        Assertions.assertThat(sqlTypeList.get(0).getValue()).isEqualTo("insert");
        Assertions.assertThat(sqlTypeList.get(1).getKey()).isEqualTo("get");
        Assertions.assertThat(sqlTypeList.get(1).getValue()).isEqualTo("select");

    }
}