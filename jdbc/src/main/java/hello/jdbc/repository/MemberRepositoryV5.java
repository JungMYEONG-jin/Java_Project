package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * 예외 누수 해결
 * 체크 예외를 runtime으로 변경
 * exception spring.. interface\
 * jdbcTemplate
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository{

    private final JdbcTemplate template;

    public MemberRepositoryV5(DataSource template) {
        this.template = new JdbcTemplate(template);
    }

    @Override
    public Member save(Member member){
        String sql = "insert into member(member_id, money) values (?,?)";
        template.update(sql, member.getMemberId(), member.getMoney());
        return member;
    }

    @Override
    public void update(String memberId, int money) {
        String sql = "update member set money=? where member_id=?";
        template.update(sql, money, memberId);

    }


    @Override
    public void delete(String memberId)  {
        String sql = "delete from member where member_id=?";
        template.update(sql, memberId);
    }



    @Override
    public Member findById(String memberId) {
        String sql = "select * from member where member_id = ?";
        return template.queryForObject(sql, memberRowMapper(), memberId);

    }

    private RowMapper<Member> memberRowMapper(){
        return (rs, rowNum) ->{
            Member member = new Member();
            member.setMemberId(rs.getString("member_id"));
            member.setMoney(rs.getInt("money"));
            return member;
        };
    }



}
