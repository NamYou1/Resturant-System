package com.saranaresturantsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_purchases")
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 55)
    private String reference;

    private LocalDateTime date = LocalDateTime.now();

    @Column(length = 1000)
    private String note;

    @Column(precision = 25, scale = 4)
    private BigDecimal total = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(name = "created_by")
    private Integer createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id") // Removed insertable/updatable = false to allow saving
    private Seller seller;

    @Column(name = "order_discount")
    private BigDecimal orderDiscount = BigDecimal.ZERO;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @Column(name = "delete_flag")
    private Integer deleteFlag = 0;

    @Column(name = "purchases_status", length = 20)
    private String purchasesStatus;

    @Column(name = "grand_total", precision = 14, scale = 4)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Column(name = "payment_status", length = 20)
    private String paymentStatus;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> items = new ArrayList<>();
    public void calculateTotals() {
        // Calculate the sum of all item subtotals
        this.total = items.stream()
                .map(item -> item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sum of all item-level discounts
        BigDecimal itemDiscounts = items.stream()
                .map(item -> item.getTotalDiscount() != null ? item.getTotalDiscount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Final calculations
        this.totalDiscount = itemDiscounts.add(this.orderDiscount != null ? this.orderDiscount : BigDecimal.ZERO);
        this.grandTotal = this.total.subtract(this.totalDiscount);
    }
}