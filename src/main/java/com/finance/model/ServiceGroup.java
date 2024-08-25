package com.finance.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_fn_service_group")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ServiceGroup extends Auditable<String> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.finance.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
}
