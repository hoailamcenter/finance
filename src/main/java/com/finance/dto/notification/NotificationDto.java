package com.finance.dto.notification;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NotificationDto {
    @ApiModelProperty(name = "id")
    private Long id;
    @ApiModelProperty(name = "massage")
    private String message;
    @ApiModelProperty(name = "state")
    private Integer state;
    @ApiModelProperty(name = "serviceId")
    private Long serviceId;
    @ApiModelProperty(name = "accountId")
    private Long accountId;
}
