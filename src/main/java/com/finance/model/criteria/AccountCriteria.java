package com.finance.model.criteria;

import com.finance.model.*;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class AccountCriteria implements Serializable{
    private Long id;
    private Integer kind;
    private String username;
    private String phone;
    private String email;
    private String fullName;
    private Integer status;
    private Long groupId;
    private Long departmentId;
    private Date birthDate;
    private String address;

    public Specification<Account> getCriteria() {
        return new Specification<Account>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Account> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(getId() != null){
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if(getKind() != null){
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if(getStatus() != null){
                    predicates.add(cb.equal(root.get("status"), getStatus()));
                }
                if(StringUtils.isNoneBlank(getUsername())){
                    predicates.add(cb.like(cb.lower(root.get("username")), "%" + getUsername().toLowerCase() + "%"));
                }
                if(StringUtils.isNoneBlank(getPhone())){
                    predicates.add(cb.like(cb.lower(root.get("phone")), "%" + getPhone().toLowerCase() + "%"));
                }
                if(StringUtils.isNoneBlank(getEmail())){
                    predicates.add(cb.like(cb.lower(root.get("email")), "%" + getEmail().toLowerCase() + "%"));
                }
                if(StringUtils.isNoneBlank(getFullName())){
                    predicates.add(cb.like(cb.lower(root.get("fullName")), "%" + getFullName().toLowerCase() + "%"));
                }
                if (getGroupId() != null){
                    Join<Account, Group> joinGroup = root.join("group", JoinType.INNER);
                    predicates.add(cb.equal(joinGroup.get("id"), getGroupId()));
                }
                if (getDepartmentId() != null){
                    Join<Account, Department> joinDepartment = root.join("department", JoinType.INNER);
                    predicates.add(cb.equal(joinDepartment.get("id"), getDepartmentId()));
                }
                if(StringUtils.isNoneBlank(getAddress())){
                    predicates.add(cb.like(cb.lower(root.get("address")), "%" + getAddress().toLowerCase() + "%"));
                }
                if (getBirthDate() != null) {
                    predicates.add(cb.equal(root.get("birthDate"), getBirthDate()));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }

}
