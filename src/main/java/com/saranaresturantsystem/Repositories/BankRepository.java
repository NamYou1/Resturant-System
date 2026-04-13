package com.saranaresturantsystem.Repositories;

import com.saranaresturantsystem.Enities.Bank;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BankRepository extends JpaRepository<Bank,Long>, JpaSpecificationExecutor<Bank> {

}
