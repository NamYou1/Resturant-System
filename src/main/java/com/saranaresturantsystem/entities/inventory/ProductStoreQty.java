package com.saranaresturantsystem.entities.inventory;

import com.saranaresturantsystem.entities.product.Product;
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
    @Column(precision = 15, scale = 4, columnDefinition = "NUMERIC(15,4) DEFAULT 0")
    private BigDecimal quantity = BigDecimal.ZERO;
    @Column(precision = 25, scale = 4)
    private BigDecimal price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

}