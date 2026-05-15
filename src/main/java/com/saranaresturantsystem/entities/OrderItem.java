package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.common.DateTimeUtils;
import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.User;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_orderitem")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "itemId")
    private Integer itemId;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "date")
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tables_id")
    private Tables tables;
}
