package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String code;

    @Column(length = 40)
    private String logo;

    @Column(length = 100)
    private String email;

    @Column(length = 25, nullable = false)
    private String phone;

    @Column(length = 200)
    private String address1;

    @Column(length = 200)
    private String address2;

    @Column(length = 20)
    private String city;

    @Column(length = 20)
    private String state;

    @Column(name = "postal_code", length = 8)
    private String postalCode;

    @Column(length = 25)
    private String country;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "receipt_header", columnDefinition = "TEXT")
    private String receiptHeader;

    @Column(name = "receipt_footer", columnDefinition = "TEXT")
    private String receiptFooter;
}