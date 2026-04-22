package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BankRepository extends JpaRepository<Bank,Long>, JpaSpecificationExecutor<Bank> {

}
