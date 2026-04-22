package com.saranaresturantsystem.repositories;


import com.saranaresturantsystem.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
