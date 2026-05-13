package com.saranaresturantsystem.repositories;

import com.saranaresturantsystem.entities.Expenses;
import com.saranaresturantsystem.entities.ExpensesType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ExpensesRepository extends JpaRepository<Expenses,Long>, JpaSpecificationExecutor<Expenses> {
}
