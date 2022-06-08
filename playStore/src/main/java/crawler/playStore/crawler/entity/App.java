package crawler.playStore.crawler.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class App {

    @Id
    @GeneratedValue
    @Column(name = "App_id")
    private Long id;

    @Column(name = "Name")
    private String name;
    private String review;
    private String reviewDate;
    private String writer;


}
