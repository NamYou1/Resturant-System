package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column( name = "c_code", length = 50, nullable = false, unique = true)
    private String code;
    @Column( name = "c_name",length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "c_display",length = 50)
    private String display;
    @Column( name =  "c_image", length = 500)
    private String imageUrl;
    @Column(name = "c_from_time")
    private LocalDate fromTime;
    @Column(name = "c_to_time")
    private LocalDate toTime;
    @Column( name = "c_status",length = 5 )
    private GeneralStatus status = GeneralStatus.ACTIVE;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategory> subcategories ;


}
