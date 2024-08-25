package com.finance.model.criteria;

import lombok.Data;

import java.util.List;

@Data
public class ExportTransactionCriteria {
    private List<Integer> states;
    private Integer kind;
    private Integer sortDate;
}
