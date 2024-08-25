package com.finance.dto.transaction;

import com.finance.dto.category.CategoryDto;
import com.finance.dto.paymentPeriod.PaymentPeriodDto;
import com.finance.dto.transactionGroup.TransactionGroupDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;
@Data
public class TransactionDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "money")
    private String money;
    @ApiModelProperty(name = "transactionDate")
    private Date transactionDate;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "document")
    private String document;
    @ApiModelProperty(name = "category")
    private CategoryDto category;
    @ApiModelProperty(name = "transactionGroup")
    private TransactionGroupDto transactionGroup;
    @ApiModelProperty(name = "paymentPeriod")
    private PaymentPeriodDto paymentPeriod;
}
