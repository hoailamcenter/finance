package com.finance.repository;

import com.finance.model.ServiceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ServiceGroupRepository extends JpaRepository<ServiceGroup, Long>, JpaSpecificationExecutor<ServiceGroup> {
    Optional<ServiceGroup> findFirstByName(String name);
}