package com.progress.finalproject.model.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_status")
@Getter @Setter @NoArgsConstructor
public class OrderStatus {
    @Id
    @Column(name = "status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int statusId;

    @Column(name = "status_name")
    private String statusName;

}
