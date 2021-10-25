package ru.gb.antonov.j710.order.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity  @Data  @NoArgsConstructor  @Table (name="orders")
public class Order
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="ouruser_id", nullable=false)
    private Long ouruserId;

    @Column (name="phone")
    private String phone;

    @Column (name="address")
    private String address;

    @Column (name="cost")
    private double cost;    //< общая стоимость выбранных/купленных товаров

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column (name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn (name="orderstate_id", nullable=false)
    private OrderState state;
//--------неколонки
    @OneToMany (mappedBy="order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderItem> orderItems;
    //У OrderItem'мов не нужно указывать cascade, т.к. мы их тянем за собой в БД,
    // а не они нас.
//----------------------------------------------------------------------
    public List<OrderItem> getOrderItems () { return Collections.unmodifiableList (orderItems); }
    public void setOrderItems (List<OrderItem> value) {    orderItems = value;   }
//----------------------------------------------------------------------
    @Override public String toString()
    {   return String.format ("Order:[id:%d, uid:%d, phone:%s, addr:%s]_with_[%s]",
                              id, ouruserId/*ouruser.getLogin()*/, phone, address, orderItems);
    }
}