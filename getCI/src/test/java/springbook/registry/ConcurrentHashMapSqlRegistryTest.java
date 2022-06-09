package springbook.registry;

import org.springframework.boot.test.context.SpringBootTest;
import kakao.getCI.springbook.user.sqlservice.updatable.ConcurrentHashMapSqlRegistry;
import kakao.getCI.springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

@SpringBootTest(classes = Runnable.class)
public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{


    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
