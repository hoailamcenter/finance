package com.finance.model.criteria;

import com.finance.constant.FinanceConstant;
import com.finance.model.Account;
import com.finance.model.Transaction;
import com.finance.model.TransactionHistory;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class TransactionHistoryCriteria {
    private Long id;
    private String note;
    private Integer state;
    private Long accountId;
    private Long transactionId;
    private Integer status;
    private Date startDate;
    private Date endDate;
    private Integer isPaged = FinanceConstant.IS_PAGED_TRUE; // 0: false, 1: true

    public Specification<TransactionHistory> getCriteria() {
        return new Specification<TransactionHistory>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<TransactionHistory> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (StringUtils.isNoneBlank(getNote())) {
                    predicates.add(cb.like(cb.lower(root.get("note")), "%" + getNote().toLowerCase() + "%"));
                }
                if (getState() != null) {
                    predicates.add(cb.equal(root.get("state"), getState()));
                }
                if (getAccountId() != null) {
                    Join<TransactionHistory, Account> joinCategory = root.join("account", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getAccountId()));
                }
                if (getTransactionId() != null) {
                    Join<TransactionHistory, Transaction> joinCategory = root.join("transaction", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getTransactionId()));
                }
                if (getStatus() != null) {
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getStartDate() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), getStartDate()));
                }
                if (getEndDate() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), getEndDate()));
                }
                query.orderBy(cb.desc(root.get("createdDate")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
