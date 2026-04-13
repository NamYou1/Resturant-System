package com.saranaresturantsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "tbl_sub_category")
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id")
    private  long id ;
    @Column(name = "sub_section" , length = 50 , unique = true)
    private  String section ;
    @Column(name = "sub_status" , length = 10)
    private GeneralStatus status = GeneralStatus.ACTIVE;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category ;

}
