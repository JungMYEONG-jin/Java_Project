package hello.jdbc.exception.translator;

import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import hello.jdbc.repository.ex.MyDbException;
import hello.jdbc.repository.ex.MyDuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {

    Repository repository;
    Service service;

    @BeforeEach
    void init(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        repository = new Repository(dataSource);
        service = new Service(repository);
    }

    @Test
    void duplicateKeySave(){
        service.create("my");
        service.create("my");
    }


    @Slf4j
    @RequiredArgsConstructor
    static class Service{
        private final Repository repository;

        public void create(String memberId){
            try{
                repository.save(new Member(memberId, 0));
                log.info("saveId={}", memberId);
            }catch (MyDuplicateKeyException e){
                log.info("Dup Key, Retry...");
                String newId = generateNewId(memberId);
                log.info("new ID={}", newId);
                repository.save(new Member(newId, 0));
            }catch (MyDbException e){
                log.info("데이터 접근 예외", e);
                throw e;
            }

        }

        private String generateNewId(String memberId){
            return memberId + new Random().nextInt(10000);
        }
    }



    @RequiredArgsConstructor
    static class Repository{

        private final DataSource dataSource;
        public Member save(Member member){
            String sql = "insert into member(member_id, money) values(?, ?)";
            Connection connection = null;
            PreparedStatement pstmt = null;
            try{
                connection = dataSource.getConnection();
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, member.getMemberId());
                pstmt.setInt(2, member.getMoney());
                pstmt.executeUpdate();
                return member;
            }catch (SQLException e){
                if (e.getErrorCode() == 23505){
                    throw new MyDuplicateKeyException(e);
                }
                throw new MyDbException(e);
            }finally {
                JdbcUtils.closeStatement(pstmt);
                JdbcUtils.closeConnection(connection);
            }
        }
    }
}
