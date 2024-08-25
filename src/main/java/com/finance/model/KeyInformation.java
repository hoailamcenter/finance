package com.finance.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = "db_fn_key_information")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class KeyInformation extends Auditable<String>  {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.finance.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private String name;
    private Integer kind; // 1: Server, 2: Google
    @Column(columnDefinition = "text")
    private String description;
    @Column(columnDefinition = "text")
    private String additionalInformation;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @ManyToOne
    @JoinColumn(name = "key_information_group_id")
    private KeyInformationGroup keyInformationGroup;
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
