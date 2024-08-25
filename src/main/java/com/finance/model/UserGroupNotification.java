package com.finance.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_fn_user_group_notification")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class UserGroupNotification extends Auditable<String> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.finance.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "notification_group_id")
    private NotificationGroup notificationGroup;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
