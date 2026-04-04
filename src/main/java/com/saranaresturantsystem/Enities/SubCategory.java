package com.saranaresturantsystem.Enities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(name = "sub_show_flag", length = 20)
    private  int showFlag ;
    @Column(name = "sub_status" , length = 5)
    private String status ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference
    private Category category ;

}
