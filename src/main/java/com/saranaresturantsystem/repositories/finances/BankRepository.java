package com.saranaresturantsystem.repositories.finances;

import com.saranaresturantsystem.entities.finance.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BankRepository extends JpaRepository<Bank,Long>, JpaSpecificationExecutor<Bank> {

}
