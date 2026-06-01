package com.saranaresturantsystem.entities.product;

import com.saranaresturantsystem.entities.sales.Group;
import com.saranaresturantsystem.enums.StatusType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_options")
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "o_name",unique = true,nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @Column(length = 255)
    private BigDecimal price;
    @Enumerated(EnumType.STRING  )
    @Column(length = 50)
    private StatusType isActive;
}
