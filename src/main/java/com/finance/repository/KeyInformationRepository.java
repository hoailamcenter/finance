package com.finance.repository;

import com.finance.model.KeyInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface KeyInformationRepository extends JpaRepository<KeyInformation, Long>, JpaSpecificationExecutor<KeyInformation> {
    List<KeyInformation> findByKeyInformationGroupId(Long id);
    @Transactional
    @Modifying
    @Query("UPDATE KeyInformation ki SET ki.account.id = NULL WHERE ki.account.id = :accountId")
    void updateAllByAccountId(@Param("accountId") Long accountId);
    @Transactional
    @Modifying
    @Query("UPDATE KeyInformation ki SET ki.keyInformationGroup.id = NULL WHERE ki.keyInformationGroup.id = :keyInformationGroupId")
    void updateAllByKeyInformationGroupId(@Param("keyInformationGroupId") Long keyInformationGroupId);
    @Transactional
    @Modifying
    @Query("UPDATE KeyInformation ki SET ki.organization.id = NULL WHERE ki.organization.id = :organizationId")
    void updateAllByOrganizationId(@Param("organizationId") Long organizationId);
    List<KeyInformation> findByOrganizationId(Long id);
}
