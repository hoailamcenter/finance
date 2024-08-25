package com.finance.model.criteria;

import com.finance.constant.FinanceConstant;
import com.finance.model.Category;
import com.finance.model.PaymentPeriod;
import com.finance.model.Transaction;
import com.finance.model.TransactionGroup;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TransactionCriteria {
    private Long id;
    private String name;
    private Integer kind;
    private Integer state;
    private Integer status;
    private Long categoryId;
    private Long transactionGroupId;
    private Long paymentPeriodId;
    private Date startDate;
    private Date endDate;
    private Integer sortDate; // 1: asc, 2: desc
    private Integer isPaged = FinanceConstant.IS_PAGED_TRUE; // 0: false, 1: true

    public Specification<Transaction> getCriteria() {
        return new Specification<Transaction>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (StringUtils.isNoneBlank(getName())) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if (getState() != null) {
                    predicates.add(cb.equal(root.get("state"), getState()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getCategoryId() != null) {
                    Join<Transaction, Category> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getCategoryId()));
                }
                if (getTransactionGroupId() != null) {
                    Join<Transaction, TransactionGroup> joinTransactionGroup = root.join("transactionGroup", JoinType.INNER);
                    predicates.add(cb.equal(joinTransactionGroup.get("id"), getTransactionGroupId()));
                }
                if (getPaymentPeriodId() != null) {
                    Join<Transaction, PaymentPeriod> joinPaymentPeriod = root.join("paymentPeriod", JoinType.INNER);
                    predicates.add(cb.equal(joinPaymentPeriod.get("id"), getPaymentPeriodId()));
                }
                if (getStartDate() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("transactionDate"), getStartDate()));
                }
                if (getEndDate() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("transactionDate"), getEndDate()));
                }
                if(getSortDate() != null){
                    if(getSortDate().equals(FinanceConstant.SORT_DATE_ASC)){
                        query.orderBy(cb.asc(root.get("createdDate")));
                    }else if (getSortDate().equals(FinanceConstant.SORT_DATE_DESC)){
                        query.orderBy(cb.desc(root.get("createdDate")));
                    }
                    else if (getSortDate().equals(FinanceConstant.SORT_TRANSACTION_DATE_ASC)){
                        query.orderBy(cb.asc(root.get("transactionDate")));
                    }
                    else if (getSortDate().equals(FinanceConstant.SORT_TRANSACTION_DATE_DESC)){
                        query.orderBy(cb.desc(root.get("transactionDate")));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
