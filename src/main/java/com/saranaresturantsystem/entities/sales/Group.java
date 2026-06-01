package com.saranaresturantsystem.entities.sales;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;
    @Column( name = "g_name",length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "g_description" )
    private String description;
    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
//    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "tableGroup")
    private List<Tables> tables;
}
