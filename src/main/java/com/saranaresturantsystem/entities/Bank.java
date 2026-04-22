package com.saranaresturantsystem.entities;

import com.saranaresturantsystem.entities.status.GeneralStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_banks")
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bank_id" )
    private Long id;
    @Column( name = "b_name",length = 50 , unique = true , nullable = false)
    private String name;
    @Column( name = "b_number",length = 50 , unique = true , nullable = false)
    private String number;
    @Column(name = "b_amount",length = 50 )
    private String amount;
    @Column(name = "b_is_default",length = 50)
    private String isDefault;
    @Column( name = "b_statement",length = 100)
    private String statement;
    @Column ( name = "b_from_time",length = 50)
    private LocalDate fromTime;
    @Column(name = "b_to_time")
    private LocalDate toTime;
    @Enumerated(EnumType.STRING)
    @Column(name = "b_status", length = 10 )
    private GeneralStatus status = GeneralStatus.ACTIVE;
}
