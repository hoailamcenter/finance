package com.finance.dto.transaction;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyStatisticTransactionDto {
    @ApiModelProperty(name = "startDate")
    private Date startDate;
    @ApiModelProperty(name = "endDate")
    private Date endDate;
    @ApiModelProperty(name = "totalTransactions")
    private Long totalTransactions;
    @ApiModelProperty(name = "totalIncome")
    private String totalIncome;
    @ApiModelProperty(name = "totalExpenditure")
    private String totalExpenditure;
}
