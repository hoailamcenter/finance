package com.finance.notification.form;

import lombok.Data;

@Data
public class PostNotificationData {
    private String message;
    private Long accountId;
}
