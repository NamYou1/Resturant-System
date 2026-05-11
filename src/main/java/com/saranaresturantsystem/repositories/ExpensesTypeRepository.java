package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.ExpensesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpensesTypeRepository extends JpaRepository<ExpensesType,Long>, JpaSpecificationExecutor<ExpensesType> {
}
