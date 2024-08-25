package com.finance.repository;

import com.finance.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long>, JpaSpecificationExecutor<TransactionHistory> {
    @Transactional
    void deleteAllByTransactionId(Long transactionId);
    @Transactional
    @Modifying
    @Query("UPDATE TransactionHistory th SET th.account.id = NULL WHERE th.account.id = :accountId")
    void updateAllByAccountId(@Param("accountId") Long accountId);
}
