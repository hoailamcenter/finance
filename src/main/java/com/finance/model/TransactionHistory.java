package com.finance.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_fn_transaction_history")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class TransactionHistory extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.finance.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    @Column(columnDefinition = "text")
    private String note;
    private Integer state; //  1: created, 2: approve, 3: reject, 4: paid
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;
}
