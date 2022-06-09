package springbook.issuetracker.sqlservice.updatable;

import kakao.getCI.springbook.issuetracker.sqlservice.updatable.EmbeddedSqlRegistry;
import org.junit.After;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import kakao.getCI.springbook.issuetracker.sqlservice.UpdatableSqlRegistry;
import springbook.registry.AbstractUpdatableSqlRegistryTest;

@SpringBootTest(classes = Runnable.class)
public class EmbeddedSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("../resources/schema.sql").build();

        EmbeddedSqlRegistry embeddedSqlRegistry = new EmbeddedSqlRegistry();
        embeddedSqlRegistry.setDataSource(db);

        return embeddedSqlRegistry;
    }

    @After
    public void tearDown(){
        db.shutdown();
    }



}