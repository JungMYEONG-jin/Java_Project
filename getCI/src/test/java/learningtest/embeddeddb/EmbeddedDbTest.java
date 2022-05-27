package learningtest.embeddeddb;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDbTest {

    EmbeddedDatabase db;
    JdbcTemplate template;

    @Before
    public void setup(){

        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("../resources/schema.sql").addScript("../resources/data.sql").build();

        template = new JdbcTemplate(db);

    }

    @After
    public void tearDown(){
        db.shutdown();
    }


}
