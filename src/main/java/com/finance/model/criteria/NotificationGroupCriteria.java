package com.finance.model.criteria;

import com.finance.constant.FinanceConstant;
import com.finance.model.NotificationGroup;
import com.finance.model.Service;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationGroupCriteria {
    private Long id;
    private String name;
    private Integer status;
    private Long serviceId;
    private Integer isPaged = FinanceConstant.IS_PAGED_TRUE; // 0: false, 1: true

    public Specification<NotificationGroup> getCriteria() {
        return new Specification<NotificationGroup>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<NotificationGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(StringUtils.isNoneBlank(getName())){
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getServiceId() != null){
                    Join<NotificationGroup, Service> joinService = root.join("service", JoinType.INNER);
                    predicates.add(cb.equal(joinService.get("id"), getServiceId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}
