package com.saranaresturantsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", insertable = false, updatable = false)
    private Store store;
    @Column(name = "store_id")
    private Integer storeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    @Column(name = "product_id")
    private Long productId;
    @Column(name = "no")
    private Integer no;
    @Column(name = "reference_no", length = 20)
    private String referenceNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;
    @Column(name = "purchase_id")
    private Integer purchaseId;
    @Column(precision = 12, scale = 2)
    private BigDecimal quantity;
    @Column(name = "unit_quantity", precision = 12, scale = 2)
    private BigDecimal unitQuantity;
    @Column(name = "quantity_perunit", precision = 12, scale = 2)
    private BigDecimal quantityPerUnit;
    @Column(name = "sale_id")
    private Integer saleId;
    @Column(name = "transfer_id")
    private Integer transferId;
    @Column(name = "adjust_id")
    private Integer adjustId;
    @Column(name = "sale_return_id")
    private Integer saleReturnId;
    @Column(name = "purchase_return_id")
    private Integer purchaseReturnId;
    @Column(length = 20)
    private String status;
    @Column(name = "create_by")
    private Integer createBy;
    @Column(length = 10)
    private String type;
    @Column(name = "total_cost", precision = 12, scale = 4)
    private BigDecimal totalCost;
    @Column(name = "total_price", precision = 12, scale = 4)
    private BigDecimal totalValue;
    private LocalDateTime date = LocalDateTime.now();
    @Column(name = "tran_date")
    private LocalDateTime tranDate;
}