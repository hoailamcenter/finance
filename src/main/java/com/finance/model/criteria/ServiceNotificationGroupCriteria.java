package com.finance.model.criteria;

import com.finance.constant.FinanceConstant;
import com.finance.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class ServiceNotificationGroupCriteria {
    private Long id;
    private Integer status;
    private Long serviceId;
    private Long notificationGroupId;
    private Integer isPaged = FinanceConstant.IS_PAGED_TRUE; // 0: false, 1: true

    public Specification<ServiceNotificationGroup> getCriteria() {
        return new Specification<ServiceNotificationGroup>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<ServiceNotificationGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if (getServiceId() != null){
                    Join<ServiceNotificationGroup, Service> joinService = root.join("service", JoinType.INNER);
                    predicates.add(cb.equal(joinService.get("id"), getServiceId()));
                }
                if (getNotificationGroupId() != null){
                    Join<ServiceNotificationGroup, NotificationGroup> joinNotificationGroup = root.join("notificationGroup", JoinType.INNER);
                    predicates.add(cb.equal(joinNotificationGroup.get("id"), getNotificationGroupId()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}