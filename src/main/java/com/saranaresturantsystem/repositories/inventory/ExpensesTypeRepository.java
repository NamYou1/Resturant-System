package com.saranaresturantsystem.repositories.inventory;

import com.saranaresturantsystem.entities.finance.ExpensesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpensesTypeRepository extends JpaRepository<ExpensesType,Long>, JpaSpecificationExecutor<ExpensesType> {
}
