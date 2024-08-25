package com.finance.model.criteria;

import com.finance.constant.FinanceConstant;
import com.finance.model.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class KeyInformationCriteria {
    private Long id;
    private String name;
    private Integer kind; // 1: Server, 2: Google
    private String username;
    private Long accountId;
    private Long keyInformationGroupId;
    private Long organizationId;
    private Integer status;
    private Integer sortDate; // 1: asc, 2: desc
    private Integer isPaged = FinanceConstant.IS_PAGED_TRUE; // 0: false, 1: true

    public Specification<KeyInformation> getCriteria() {
        return new Specification<KeyInformation>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<KeyInformation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
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
                if (StringUtils.isNoneBlank(getUsername())) {
                    predicates.add(cb.like(cb.lower(root.get("username")), "%" + getUsername().toLowerCase() + "%"));
                }
                if (getAccountId() != null) {
                    Join<KeyInformation, Account> joinAccount = root.join("account", JoinType.LEFT);
                    predicates.add(cb.equal(joinAccount.get("id"), getAccountId()));
                }
                if (getKeyInformationGroupId() != null) {
                    Join<KeyInformation, KeyInformationGroup> joinKeyInformationGroup = root.join("keyInformationGroup", JoinType.LEFT);
                    predicates.add(cb.equal(joinKeyInformationGroup.get("id"), getKeyInformationGroupId()));
                }
                if (getOrganizationId() != null) {
                    Join<KeyInformation, Organization> joinOrganization = root.join("organization", JoinType.LEFT);
                    predicates.add(cb.equal(joinOrganization.get("id"), getOrganizationId()));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
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