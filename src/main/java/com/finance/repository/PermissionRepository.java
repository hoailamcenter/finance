package com.finance.repository;

import com.finance.model.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    Page<Permission> findAllByIsSystem(Boolean isSystem, Pageable pageable);
    Permission findFirstByName(String name);
    @Transactional
    @Modifying
    @Query("UPDATE Permission p SET p.isSystem = :isSystem")
    void updateAllByIsSystem(@Param("isSystem") Boolean isSystem);
}
