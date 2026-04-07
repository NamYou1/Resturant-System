package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_product_store_qty")
public class ProductStoreQty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "store_id", nullable = false)
    private Integer storeId;

    @Column(precision = 15, scale = 4, columnDefinition = "NUMERIC(15,4) DEFAULT 0")
    private BigDecimal quantity = BigDecimal.ZERO;

    @Column(precision = 25, scale = 4)
    private BigDecimal price;
}