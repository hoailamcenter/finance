package com.finance.repository;

import com.finance.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository extends JpaRepository<Service, Long>, JpaSpecificationExecutor<Service> {
    Optional<Service> findFirstByNameAndKind(String name, Integer kind);
    @Transactional
    @Modifying
    @Query("UPDATE Service s SET s.organization.id = NULL WHERE s.organization.id = :organizationId")
    void updateAllByOrganizationId(@Param("organizationId") Long organizationId);
    @Transactional
    @Modifying
    @Query("UPDATE Service s SET s.category.id = NULL WHERE s.category.id = :categoryId")
    void updateAllByCategoryId(@Param("categoryId") Long categoryId);
    @Transactional
    @Modifying
    @Query("UPDATE Service s SET s.serviceGroup.id = NULL WHERE s.serviceGroup.id = :serviceGroupId")
    void updateAllByServiceGroupId(@Param("serviceGroupId") Long serviceGroupId);

    List<Service> findAllByServiceGroupId(Long serviceGroupId);
}
