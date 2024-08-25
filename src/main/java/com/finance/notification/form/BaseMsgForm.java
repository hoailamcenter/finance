package com.finance.notification.form;

import lombok.Data;

@Data
public class BaseMsgForm<T> {
    private String app;
    private String cmd;
    private String token;
    private T data;
}
