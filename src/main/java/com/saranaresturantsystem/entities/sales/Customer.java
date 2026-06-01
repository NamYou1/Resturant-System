package com.saranaresturantsystem.entities.sales;

import com.saranaresturantsystem.entities.BaseEntity;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "tbl_customers")
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String phone;
    private String email;
    private String address;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", nullable = false)
    private Group tableGroup;
    @Column(name = "table_group_name")
    private String tableGroupName;
    @Column(name = "price_group_id", nullable = false)
    private Long priceGroupId;
    @Column(name = "price_group_name")
    private String priceGroupName;
    @Column(name = "customer_group_id")
    private Long customerGroupId;
    @Enumerated(EnumType.STRING)
    private StatusType status;

}
