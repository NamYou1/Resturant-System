package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_expenses")
public class Expenses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "e_name", length = 255, unique = true, nullable = false)
    private String name;

    @Column(name = "e_reference", length = 255, unique = true, nullable = false)
    private String reference;

    @Column(name = "e_date")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private StatusType status;
    @Column(name = "e_amount", precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "e_note", length = 255)
    private String note;

    @Column(name = "e_attachment", length = 255)
    private String attachment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id", nullable = false)
    private Bank bank;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenses_type_id", nullable = false)
    private ExpensesType expensesType;

    @Column(name = "create_by")
    private Integer createBy;
}
