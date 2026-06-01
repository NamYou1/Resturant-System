package com.saranaresturantsystem.entities.inventory;

import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.product.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_adjustment_items")
public class AdjustmentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjust_id")
    private Adjustment adjustment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
    private BigDecimal quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Column(name = "unit_quantity")
    private BigDecimal unitQuantity;

    @Column(name = "real_unit_cost")
    private BigDecimal realUnitCost;
    private BigDecimal subtotal;
    @Column(name = "quantity_perunit")
    private BigDecimal quantityPerUnit;

}
