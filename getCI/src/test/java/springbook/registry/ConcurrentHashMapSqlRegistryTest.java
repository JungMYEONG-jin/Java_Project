package springbook.registry;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import springbook.exception.SqlNotFoundException;
import springbook.user.sqlservice.updatable.ConcurrentHashMapSqlRegistry;
import springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

@SpringBootTest(classes = Runnable.class)
public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{


    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
