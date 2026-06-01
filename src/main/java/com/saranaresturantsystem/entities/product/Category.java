package com.saranaresturantsystem.entities.product;

import com.saranaresturantsystem.entities.BaseEntity;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_categories")
public class Category extends BaseEntity {
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
    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
    @OneToMany(mappedBy = "category")
    private List<SubCategory> subcategories ;
    @OneToMany(mappedBy = "category")
    private List<Product> tblProduct;
}
