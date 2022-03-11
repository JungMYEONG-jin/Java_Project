package domain;

import javax.persistence.*;

// jpa 기본전략은 single table
// joined like jpa 방법

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn //dtype을 만들어줌
public abstract class Item {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
