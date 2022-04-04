package hello.hellospring.service;

import hello.hellospring.domain.Address;
import hello.hellospring.domain.Member;
import hello.hellospring.domain.Order;
import hello.hellospring.domain.OrderStatus;
import hello.hellospring.domain.item.Book;
import hello.hellospring.domain.item.Item;
import hello.hellospring.exception.NotEnoughStockException;
import hello.hellospring.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {


    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    public Member createMember()
    {
        Member member = new Member();
        member.setName("JJM");
        member.setAddress(new Address("seoul","karea", "123"));
        em.persist(member);

        return member;
    }

    public Book createBook(String jpa, int price, int quantity)
    {
        Book book = new Book();
        book.setName(jpa);
        book.setPrice(price);
        book.setStockQuantity(quantity);
        em.persist(book);
        return book;
    }


    @Test
    public void order_product() throws Exception
    {
        Member member = createMember();

        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertThat(OrderStatus.ORDER).isEqualTo(getOrder.getStatus());
        Assertions.assertThat(1).isEqualTo(getOrder.getOrderItems().size());
        Assertions.assertThat(10000*orderCount).isEqualTo(getOrder.getTotalPrice());
        Assertions.assertThat(8).isEqualTo(book.getStockQuantity());


    }

    @Test
    public void order_cancel() throws Exception
    {
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        orderService.cancel(orderId);

        Order getOrder = orderRepository.findOne(orderId);

        Assertions.assertThat(OrderStatus.CANCEL).isEqualTo(getOrder.getStatus());
        Assertions.assertThat(10).isEqualTo(book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void exceed_stock() throws Exception
    {
        Member member = createMember();
        Book book = createBook("JPA", 10000, 10);

        int orderCount = 11;

        orderService.order(member.getId(), book.getId(), orderCount);
    }

}