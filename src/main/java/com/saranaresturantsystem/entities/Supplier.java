package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_suppliers")

public class Supplier {
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
    @Column(name = "sup_status" , length = 10 , nullable = false)
    private GeneralStatus status = GeneralStatus.ACTIVE;
}
