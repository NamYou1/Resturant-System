package com.saranaresturantsystem.entities.finance;

import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.entities.product.Product;
import com.saranaresturantsystem.entities.product.Unit;
import com.saranaresturantsystem.enums.SaleStatus;
import com.saranaresturantsystem.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private  Long id ;
    private LocalDateTime tranDate ;
    private  String referenceNo ;
    // this is reference to another table like sale , purchase , adjustment , transfer
    @Enumerated(EnumType.STRING )
    @Column(length = 50)
    private TransactionType transactionType ;
    private  Long referenceId ;
    private BigDecimal quantity ;
    private  BigDecimal unitQuantity ;
    private  BigDecimal pricePerUnit ;
    private  BigDecimal totalAmount ;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private SaleStatus status ;
    private  String createdBy ;
    // relationship with another table
    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "store_id" , nullable = false)
    private Store tblStore ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit tblUnit  ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id" , nullable = false)
    private Product tblProduct ;
}