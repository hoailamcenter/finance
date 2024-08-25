package com.finance.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ResponseListMyTransactionDto<T> {
    private T content;
    private String totalIncome;
    private String totalExpenditure;
    private Long totalElements;
    private Integer totalPages;
}