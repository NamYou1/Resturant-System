package com.saranaresturantsystem.Enities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.Instant;
import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "table_categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false, unique = true)
    private String code;
    @Column(length = 100, nullable = false, unique = true)
    private String name;
    @Column(length = 50, nullable = true)
    private String display;
    @Column(length = 500, nullable = true)
    private String imageUrl;
    private LocalDate fromTime;
    private LocalDate toTime;
    @Column(length = 5 , nullable = false)
    private  String status ;


}
