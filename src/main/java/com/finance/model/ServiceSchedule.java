package com.finance.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_fn_service_schedule")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ServiceSchedule extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.finance.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private Integer numberOfDueDays;
    private Integer ordering;
    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;
}
