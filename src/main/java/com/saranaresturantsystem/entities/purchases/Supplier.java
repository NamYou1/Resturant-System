package com.saranaresturantsystem.entities.purchases;

import com.saranaresturantsystem.entities.BaseEntity;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_suppliers")
public class Supplier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private  Long id ;
    @Column(name = "sup_name" , length = 100 , nullable = false ,unique = true )
    private  String name;
    @Column(name = "sup_address1")
    private  String addressOne;
    @Column(name = "sup_address2"  )
    private  String addressTwo;
    @Column(name = "sup_phone" , length = 20 , nullable = false , unique = true)
    private  String phone ;
    @Column(name = "sup_email" , length = 50  , unique = true)
    private  String email ;
    @Column(name = "sup_address" )
    private String address ;
    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
}
