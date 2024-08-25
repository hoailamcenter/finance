package com.finance.model.criteria;

import com.finance.constant.FinanceConstant;
import com.finance.model.Category;
import com.finance.model.Service;
import com.finance.model.ServiceGroup;
import com.finance.model.Transaction;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ServiceCriteria {
    private Long id;
    private String name;
    private Integer kind;
    private Integer periodKind;
    private Integer status;
    private Long categoryId;
    private Long serviceGroupId;
    private Date fromDate;
    private Date toDate;
    private Integer sortDate; // 1: asc by created date, 2: desc by created date, 3: asc by due date, 4: desc by due date
    private Integer isPaged = FinanceConstant.IS_PAGED_TRUE; // 0: false, 1: true

    public Specification<Service> getCriteria() {
        return new Specification<Service>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Service> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(StringUtils.isNoneBlank(getName())){
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }
                if(getKind() != null){
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(getPeriodKind() != null){
                    predicates.add(cb.equal(root.get("periodKind"), getPeriodKind()));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getCategoryId() != null){
                    Join<Service, Category> joinCategory = root.join("category", JoinType.INNER);
                    predicates.add(cb.equal(joinCategory.get("id"), getCategoryId()));
                }
                if (getServiceGroupId() != null){
                    Join<Service, ServiceGroup> joinServiceGroup = root.join("serviceGroup", JoinType.INNER);
                    predicates.add(cb.equal(joinServiceGroup.get("id"), getServiceGroupId()));
                }
                if (getFromDate() != null) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), getFromDate()));
                }
                if (getToDate() != null) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), getToDate()));
                }
                if(getSortDate() != null){
                    if(getSortDate().equals(FinanceConstant.SORT_DATE_ASC)){
                        query.orderBy(cb.asc(root.get("createdDate")));
                    }else{
                        query.orderBy(cb.desc(root.get("createdDate")));
                    }
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
