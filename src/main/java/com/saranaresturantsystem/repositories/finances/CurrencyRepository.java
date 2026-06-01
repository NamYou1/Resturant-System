package com.saranaresturantsystem.repositories.finances;

import com.saranaresturantsystem.entities.finance.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CurrencyRepository extends JpaRepository<Currency , Long> , JpaSpecificationExecutor<Currency> {
}
