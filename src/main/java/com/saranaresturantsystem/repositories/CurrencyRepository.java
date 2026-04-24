package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurrencyRepository extends JpaRepository<Currency , Long> , JpaSpecificationExecutor<Currency> {
}
