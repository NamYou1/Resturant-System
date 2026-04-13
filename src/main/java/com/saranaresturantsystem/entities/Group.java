package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.GeneralStatus;
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
    @Column(name = "g_status", length = 5)
    private GeneralStatus status = GeneralStatus.ACTIVE;
//    @OneToMany(mappedBy = "tableGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "tableGroup")
    private List<Tables> tables;
}
