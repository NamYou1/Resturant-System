package com.saranaresturantsystem.entities.sales;

import com.saranaresturantsystem.entities.BaseEntity;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "tbl_sellers")
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(length = 100)
    private String email;
    @Column(length = 20)
    private String phone;
    @Column(length = 50, nullable = false)
    private String address;
    @Enumerated(EnumType.STRING)
    @Column( length = 10 )
    private StatusType status ;
}
