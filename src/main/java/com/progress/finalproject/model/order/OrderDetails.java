package com.progress.finalproject.model.order;

import com.progress.finalproject.model.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "order_details")
@Getter @Setter @NoArgsConstructor
public class OrderDetails {
    @Id
    @Column(name = "order_details_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderDetailsId;

    @Column(name = "order_id")
    private long orderId;

    @Column(name = "product_id")
    private long productId;

    @Column(name = "quantity")
    private long quantity;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "vat")
    private BigDecimal vat;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false)
    private Product product;











}
