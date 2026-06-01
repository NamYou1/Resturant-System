package com.saranaresturantsystem.repositories.inventory;

import com.saranaresturantsystem.entities.finance.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
