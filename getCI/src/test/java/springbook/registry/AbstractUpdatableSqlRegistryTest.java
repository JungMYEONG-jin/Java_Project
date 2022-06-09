package springbook.registry;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import kakao.getCI.springbook.exception.SqlNotFoundException;
import kakao.getCI.springbook.issuetracker.sqlservice.UpdatableSqlRegistry;

public abstract class AbstractUpdatableSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    @Before
    public void setup(){
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("key1", "sql1");
        sqlRegistry.registerSql("key2", "sql2");
        sqlRegistry.registerSql("key3", "sql3");
    }

    @Test
    public void find(){
        checkFindResult("sql1", "sql2", "sql3");
    }

    public void checkFindResult(String exp1, String exp2, String exp3){
        Assertions.assertThat(sqlRegistry.findSql("key1")).isEqualTo(exp1);
        Assertions.assertThat(sqlRegistry.findSql("key2")).isEqualTo(exp2);
        Assertions.assertThat(sqlRegistry.findSql("key3")).isEqualTo(exp3);
    }

    @Test
    public void exceptionTest(){
        org.junit.jupiter.api.Assertions.assertThrows(SqlNotFoundException.class, ()-> sqlRegistry.findSql("1111"));
    }

    @Test
    public void updageSingle(){
        sqlRegistry.updateSql("key2", "modi222");
        checkFindResult("sql1", "modi222", "sql3");
    }



}
