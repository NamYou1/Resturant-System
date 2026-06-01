package com.saranaresturantsystem.entities.product;

import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

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
    @Column(name = "sub_section" , length = 50 , unique = true , nullable = false)
    private  String section ;
    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
//    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category ;

    // product
        @OneToMany(mappedBy = "section")
    private List<Product> tblProduct ;

}
