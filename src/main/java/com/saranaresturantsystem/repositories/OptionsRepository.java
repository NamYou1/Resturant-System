package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Bank;
import com.saranaresturantsystem.entities.Options;
import com.saranaresturantsystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface OptionsRepository extends JpaRepository<Options,Long>, JpaSpecificationExecutor<Options> {


}
