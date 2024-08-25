package com.finance.form.transaction;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@Data
public class UpdateTransactionForm {
    @ApiModelProperty
    private Long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    private Integer kind;
    @ApiModelProperty
    private Integer state;
    @ApiModelProperty
    private String money;
    @ApiModelProperty
    private Date transactionDate;
    @ApiModelProperty
    private String note;
    @ApiModelProperty
    private String document;
    @ApiModelProperty
    private Long categoryId;
    @ApiModelProperty
    private Long transactionGroupId;
    @ApiModelProperty
    private Long paymentPeriodId;
}
