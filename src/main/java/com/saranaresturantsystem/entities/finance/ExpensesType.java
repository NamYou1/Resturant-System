package com.saranaresturantsystem.entities.finance;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_expensestype")
public class ExpensesType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="e_name",length = 255,unique = true,nullable = false)
    private String name;
    @Column(name = "e_description",length = 255,unique = true,nullable = false)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name="e_status",nullable = false)
    private StatusType status;
}
