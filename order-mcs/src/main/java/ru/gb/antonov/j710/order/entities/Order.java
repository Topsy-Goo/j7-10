package ru.gb.antonov.j710.order.entities;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@Table (name="orders")
public class Order {

    @Id    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="ouruser_id", nullable=false)
    private Long ouruserId;

    @ManyToOne (cascade = {CascadeType.PERSIST})
    @JoinColumn (name="shipping_info_id", nullable=false)
    private ShippingInfo shippingInfo;

    @Column (name="all_items_cost", nullable=false)
    private BigDecimal allItemsCost = BigDecimal.ZERO;

    @ManyToOne    @JoinColumn (name="orderstate_id", nullable=false)
    private OrderState state;

    @CreationTimestamp    @Column(name="created_at")
    private LocalDateTime createdAt;

    @CreationTimestamp    @Column (name="updated_at")
    private LocalDateTime updatedAt;

//--------неколонки
    @OneToMany (mappedBy="order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<OrderItem> orderItems;
//----------------------------------------------------------------------
    public Order () {}
    public List<OrderItem> getOrderItems () {
        return Collections.unmodifiableList (orderItems);
    }
//----------------------------------------------------------------------
    @Override public String toString() {
        return String.format ("Order:[id:%d, uid:%d, cost:%.2f, ph:%s, adr:%s]_with_[%s]",
                              id, ouruserId, allItemsCost,
                              shippingInfo.getPhone(), shippingInfo.getAddress(), orderItems);
    }
}
