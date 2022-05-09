package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;


//access level protected 까지만 가능함 jpa는

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of={"id", "username", "age"})// 가급적 연관관계 필드는 넣지 말자 무한루프 걸릴 수 있음
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    // x to one은 무조건 lazy setting
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team!=null)
            changeTeam(team);
    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    public void changeTeam(Team team)
    {
        this.team = team;
        team.getMembers().add(this);
    }
}
