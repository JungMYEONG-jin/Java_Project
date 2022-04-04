package hello.hellospring.domain.item;

import hello.hellospring.domain.Category;
import hello.hellospring.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // business logic

    /**
     * stock increase
     * @param quantity
     */
    public void addStock(int quantity)
    {
        this.stockQuantity+=quantity;
    }

    /**
     * stock decrrease
     * @param quantity
     */
    public void removeStock(int quantity)
    {
        int resStock = this.stockQuantity-quantity;
        if(resStock<0)
        {
            throw new NotEnoughStockException("nned more stock");
        }
        this.stockQuantity = resStock;
    }

}
