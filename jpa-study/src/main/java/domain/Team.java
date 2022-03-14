package domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseEntity{

    @Id
    @GeneratedValue
    private Long id;

//    @OneToMany
//    @JoinColumn(name="team_id")
//    private List<Member> members = new ArrayList<>();


}
