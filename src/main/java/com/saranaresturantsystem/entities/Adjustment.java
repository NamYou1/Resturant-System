package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_adjustments")
public class Adjustment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adjustment_id")
    private Long id;
    private LocalDateTime date;
    @Column(name = "reference_no",length = 100,unique = true, nullable = false)
    private String referenceNo;
    @Enumerated(EnumType.STRING)
    private StatusType status ;
    private BigDecimal total;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id",nullable = false)
    private Store store;
    private String note;
    @Column(name = "create_by", nullable = false)
    private Integer createBy;
    @Column(name = "delete_by", nullable = false)
    private Integer deleteBy;
    private String file;
    @OneToMany(mappedBy = "adjustment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdjustmentItem> items;

}
