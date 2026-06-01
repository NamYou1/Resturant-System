package com.saranaresturantsystem.repositories.sales;

import com.saranaresturantsystem.entities.product.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OptionsRepository extends JpaRepository<Options,Long>, JpaSpecificationExecutor<Options> {


}
