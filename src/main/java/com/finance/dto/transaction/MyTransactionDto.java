package com.finance.dto.transaction;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MyTransactionDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "kind")
    private Integer kind;
    @ApiModelProperty(name = "money")
    private String money;
    @ApiModelProperty(name = "note")
    private String note;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "categoryId")
    private Long categoryId;
    @ApiModelProperty(name = "categoryName")
    private String categoryName;
    @ApiModelProperty(name = "transactionDate")
    private Date transactionDate;
}
