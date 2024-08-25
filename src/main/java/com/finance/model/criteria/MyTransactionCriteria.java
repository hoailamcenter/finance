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
public class MyTransactionCriteria {
    private Date startDate;
    private Date endDate;
    private List<Integer> states;
    private Integer sortDate;
    private Integer kind;
    private Long paymentPeriodId;

    public Specification<Transaction> getCriteria() {
        return new Specification<Transaction>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if (getStates() != null && !getStates().isEmpty()) {
                    predicates.add(root.get("state").in(getStates()));
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
