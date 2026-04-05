package com.saranaresturantsystem.Enities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "table_tables")
public class TableEnities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tableId;
    @Column(length = 255 , nullable = false ,unique = true )
    private String name;
    @Column(length = 150 , nullable = false ,unique = true )
    private String orderNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id") // The FK in the database
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Group tableGroup;
}
