package com.saranaresturantsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_tables")
public class Tables {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Long tableId;
    @Column(name = "tbl_name", nullable = false ,unique = true )
    private String name;
    @Column(length = 150 , nullable = false ,unique = true , name = "tbl_order_number")
    private String orderNumber;
    @Column(name = "tbl_status", length = 5)
    private GeneralStatus status = GeneralStatus.ACTIVE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id") // The FK in the database
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Group tableGroup;
}
