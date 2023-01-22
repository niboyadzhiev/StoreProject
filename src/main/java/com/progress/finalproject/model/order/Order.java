package com.progress.finalproject.model.order;

import com.progress.finalproject.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@Getter @Setter @NoArgsConstructor
public class Order implements Comparable<Order> {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "order_date")
    private Timestamp orderDate;

    @Column(name = "order_status")
    private long statusId;

    @ManyToOne
    @JoinColumn(name = "order_status", referencedColumnName = "status_id", insertable = false, updatable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    private User user;


    @Override
    public int compareTo(Order o) {
        // the minus is to sort in descending order
        return  -(int)(this.orderId - o.getOrderId());
    }
}
