package com.saranaresturantsystem.entities.purchases;

import com.saranaresturantsystem.entities.BaseEntity;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.entities.sales.Seller;
import com.saranaresturantsystem.enums.PaymentStatus;
import com.saranaresturantsystem.enums.PurchaseStatus;
import com.saranaresturantsystem.enums.StatusType;
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
public class Purchase extends BaseEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @Column(name = "order_discount")
    private BigDecimal orderDiscount = BigDecimal.ZERO;

    @Column(name = "total_discount")
    private BigDecimal totalDiscount = BigDecimal.ZERO;
    @Column(name = "grand_total", precision = 14, scale = 4)
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PurchaseStatus purchasesStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private StatusType status;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItem> items = new ArrayList<>();

    public void calculateTotals() {
        this.total = items.stream()
                .map(item -> item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal itemDiscounts = items.stream()
                .map(item -> item.getTotalDiscount() != null ? item.getTotalDiscount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalDiscount = itemDiscounts.add(this.orderDiscount != null ? this.orderDiscount : BigDecimal.ZERO);
        this.grandTotal = this.total.subtract(this.totalDiscount);
    }
}