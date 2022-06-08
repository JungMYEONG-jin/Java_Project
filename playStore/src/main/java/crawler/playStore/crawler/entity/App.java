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
    @Column(name = "Review_Number")
    private Long id;

    @Column(name = "Name")
    private String name;
    @Column(name = "UpdateDate")
    private String updateDate;
    @Column(name = "Version")
    private String version;

    @Column(name = "Writer")
    private String writer;
    @Column(name = "ReviewDate")
    private String reviewDate;
    @Column(name = "Review", length = 1000)
    private String review;

    public App(){}

    public App(String name, String updateDate, String version, String writer, String reviewDate, String review) {
        this.name = name;
        this.updateDate = updateDate;
        this.version = version;
        this.writer = writer;
        this.reviewDate = reviewDate;
        this.review = review;
    }

    @Override
    public String toString() {
        return "App{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", version='" + version + '\'' +
                ", writer='" + writer + '\'' +
                ", reviewDate='" + reviewDate + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}
