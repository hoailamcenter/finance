package com.finance.repository;

import com.finance.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    Optional<Account> findFirstByUsername(String username);
    Optional<Account> findFirstByEmail(String email);
    Optional<Account> findFirstByPhone(String phone);
    Optional<Account> findFirstBySecretKey(String secretKey);
    @Transactional
    @Modifying
    @Query("UPDATE Account SET department.id = NULL WHERE department.id = :departmentId")
    void updateAllByDepartmentId(@Param("departmentId") Long departmentId);
    @Transactional
    @Modifying
    @Query("UPDATE Account SET department.id = NULL")
    void updateAllAccountByDepartmentToNull();
    @Transactional
    @Modifying
    @Query("UPDATE Account SET isSuperAdmin = :isSuperAdmin WHERE id = :id")
    void updateAccountByIdAndIsSuperAdmin(@Param("id") Long id, @Param("isSuperAdmin") Boolean isSuperAdmin);
}
