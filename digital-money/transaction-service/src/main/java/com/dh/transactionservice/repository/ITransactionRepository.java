package com.dh.transactionservice.repository;

import com.dh.transactionservice.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction,Long> {
    @Query("SELECT t FROM Transaction t WHERE t.account_id= :accountId ORDER BY t.date DESC LIMIT 5")
    Optional<List<Transaction>> findLatestTransactionsByAccountId(@Param("accountId") Long accountId);

}
