package com.saranaresturantsystem.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime date;
    @Column(name = "customer_id", nullable = false)
    private Integer customerId;
    @Column(name = "customer_name", length = 55, nullable = false)
    private String customerName;
    @Column(precision = 25, scale = 4, nullable = false)
    private BigDecimal total;
    @Column(name = "product_discount", precision = 25, scale = 4)
    private BigDecimal productDiscount;
    @Column(name = "order_discount_id", length = 20)
    private String orderDiscountId;
    @Column(name = "order_discount", precision = 25, scale = 4)
    private BigDecimal orderDiscount;
    @Column(name = "total_discount", precision = 25, scale = 4)
    private BigDecimal totalDiscount;
    @Column(name = "product_tax", precision = 25, scale = 4)
    private BigDecimal productTax;
    @Column(name = "order_tax_id", length = 20)
    private String orderTaxId;
    @Column(name = "order_tax", precision = 25, scale = 4)
    private BigDecimal orderTax;
    @Column(name = "total_tax", precision = 25, scale = 4)
    private BigDecimal totalTax;
    @Column(name = "grand_total", precision = 25, scale = 4, nullable = false)
    private BigDecimal grandTotal;
    @Column(name = "total_items")
    private Integer totalItems;
    @Column(name = "total_quantity", precision = 15, scale = 4)
    private BigDecimal totalQuantity;
    private BigDecimal paid;
    @Column(name = "created_by")
    private Integer createdBy;
    @Column(name = "updated_by")
    private Integer updatedBy;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(length = 1000)
    private String note;
    @Column(length = 20)
    private String status;
    private BigDecimal rounding;
    @Column(name = "store_id", nullable = false)
    private Integer storeId = 1;
    @Column(name = "hold_ref")
    private String holdRef;
    private Integer no;
    @Column(name = "delete_flag")
    private Integer deleteFlag = 0;
    @Column(name = "delete_by")
    private Integer deleteBy;
    private Short pos = 0;
    @Column(name = "sale_status", length = 50)
    private String saleStatus;
    @Column(name = "total_people", length = 10)
    private String totalPeople;
    private Integer client;
    @Column(name = "voucher_value", precision = 14, scale = 3)
    private BigDecimal voucherValue;
    @Column(name = "order_voucher", length = 10)
    private String orderVoucher;
    @Column(name = "voucher_code")
    private Integer voucherCode;
    @Column(name = "print_count")
    private Integer printCount;
    @Column(precision = 10, scale = 4)
    private BigDecimal deposit;
    @Column(name = "waiting_no", length = 50)
    private String waitingNo;
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SaleItem> items;
}