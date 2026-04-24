package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id ;
    @Column(unique = true , nullable = false , length = 50)
    private  String code ;
    @Column(unique = true , nullable = false , length = 50)
    private  String name ;
    @Column(  length = 20)
    private  String operation ;
    private  double rate ;
    @Column(length = 50)
    private  String symbol ;

}
