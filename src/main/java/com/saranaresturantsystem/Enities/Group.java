package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "table_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String name;
    private String description;
//    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "tableGroup")
    private List<TableEnities> tables;
}
