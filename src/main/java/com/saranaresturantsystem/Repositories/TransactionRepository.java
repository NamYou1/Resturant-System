package com.saranaresturantsystem.Repositories;

import com.saranaresturantsystem.Enities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
