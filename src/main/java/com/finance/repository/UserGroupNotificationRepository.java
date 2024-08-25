package com.finance.repository;

import com.finance.model.UserGroupNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserGroupNotificationRepository extends JpaRepository<UserGroupNotification, Long>, JpaSpecificationExecutor<UserGroupNotification> {
    Optional<UserGroupNotification> findFirstByAccountIdAndNotificationGroupId(Long accountId, Long notificationGroupId);
    @Transactional
    void deleteAllByNotificationGroupId(Long notificationGroupId);
    @Transactional
    void deleteAllByAccountId(Long accountId);
}
