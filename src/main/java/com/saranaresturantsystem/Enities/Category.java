package com.saranaresturantsystem.Enities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column( name = "category_code", length = 50, nullable = false, unique = true)
    private String code;
    @Column( name = "category_name",length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "category_display",length = 50, nullable = true)
    private String display;
    @Column( name =  "category_image", length = 500, nullable = true)
    private String imageUrl;
    @Column(name = "category_from_time")
    private LocalDate fromTime;
    @Column(name = "category_to_time")
    private LocalDate toTime;
    @Column( name = "category_status",length = 5 , nullable = false)
    private  String status ;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonManagedReference
    private List<SubCategory> subcategories = new ArrayList<>();


}
