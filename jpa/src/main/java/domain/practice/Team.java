//package domain.practice;
//
//import domain.real.Member;
//
//import javax.persistence.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//public class Team {
//
//    @Id
//    @GeneratedValue
//    @Column(name="team_id")
//    private Long id;
//
//    private String name;
//
//
//    @OneToMany(mappedBy = "team")
//    List<Member> members = new ArrayList<>();
//    // joincolumn 안넣으면 테이블을 만들어서 도출해냄
//    // 작으면 좋지만 크면 안좋아질수도
//    // jpa는 다대일 방향이 일대다보다 더 효율적
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public List<Member> getMembers() {
//        return members;
//    }
//
//    public void setMembers(List<Member> members) {
//        this.members = members;
//    }
//
//
//
//
//}
