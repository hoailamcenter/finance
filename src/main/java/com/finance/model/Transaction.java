package com.finance.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "db_fn_transaction")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Transaction extends Auditable<String>{
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.finance.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private String name;
    private Integer kind; // 1: income, 2: expenditure
    private Integer state; //  1: created, 2: approve, 3: reject, 4: paid
    private String money;
    private Date transactionDate;
    @Column(columnDefinition = "text")
    private String note;
    @Column(columnDefinition = "text")
    private String document;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "transaction_group_id")
    private TransactionGroup transactionGroup;
    @ManyToOne
    @JoinColumn(name = "payment_period_id")
    private PaymentPeriod paymentPeriod;
}
