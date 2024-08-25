package com.finance.constant;

import com.finance.utils.ConfigurationService;

public class FinanceConstant {
    public static final String ROOT_DIRECTORY =  ConfigurationService.getInstance().getString("file.upload-dir","/tmp/upload");

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";

    public static final String NAME_PATTERN = "^[\\p{L} ]+$";
    public static final String USERNAME_PATTERN = "^[a-z0-9_]+$";
    public static final String PASSWORD_PATTERN = "^[a-zA-Z0-9!@#$%^&*()_+-=]+$";
    public static final String PHONE_PATTERN = "^0[35789][0-9]{8}$";
    public static final String EMAIL_PATTERN = "^(?!.*[.]{2,})[a-zA-Z0-9.%]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public static final Integer USER_KIND_ADMIN = 1;
    public static final Integer USER_KIND_MANAGER = 2;

    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;
    public static final Integer STATUS_LOCK = -1;
    public static final Integer STATUS_DELETE = -2;

    public static final Integer GROUP_KIND_ADMIN = 1;
    public static final Integer GROUP_KIND_MANAGER = 2;

    public static final Integer MAX_ATTEMPT_FORGET_PWD = 5;
    public static final int MAX_TIME_FORGET_PWD = 5 * 60 * 1000;
    public static final Integer MAX_ATTEMPT_LOGIN = 5;

    public static final Boolean AES_ZIP_ENABLE = false;

    public static final Integer CATEGORY_KIND_INCOME = 1;
    public static final Integer CATEGORY_KIND_EXPENDITURE = 2;

    public static final Integer TRANSACTION_KIND_INCOME = 1;
    public static final Integer TRANSACTION_KIND_EXPENDITURE = 2;

    public static final Integer TRANSACTION_STATE_CREATED = 1;
    public static final Integer TRANSACTION_STATE_APPROVE = 2;
    public static final Integer TRANSACTION_STATE_REJECT = 3;
    public static final Integer TRANSACTION_STATE_PAID = 4;

    public static final Integer SORT_DATE_ASC = 1;
    public static final Integer SORT_DATE_DESC = 2;
    public static final Integer SORT_TRANSACTION_DATE_ASC = 3;
    public static final Integer SORT_TRANSACTION_DATE_DESC = 4;

    public static final Integer SERVICE_PERIOD_KIND_FIX_DAY = 1;
    public static final Integer SERVICE_PERIOD_KIND_MONTH = 2;
    public static final Integer SERVICE_PERIOD_KIND_YEAR = 3;

    public static final Integer NOTIFICATION_STATE_SENT = 0;
    public static final Integer NOTIFICATION_STATE_READ = 1;

    public static final Integer KEY_INFORMATION_KIND_SERVER = 1;
    public static final Integer KEY_INFORMATION_KIND_WEB = 2;

    public static final String PRIVATE_KEY = "private_key";
    public static final String FINANCE_SECRET_KEY = "finance_secret_key";
    public static final String KEY_INFORMATION_SECRET_KEY = "key_information_secret_key";

    public static final Integer SERVICE_KIND_INCOME = 1;
    public static final Integer SERVICE_KIND_EXPENDITURE = 2;

    public static final Integer PAYMENT_PERIOD_STATE_CREATED = 1;
    public static final Integer PAYMENT_PERIOD_STATE_APPROVE = 2;

    public static final Integer IS_PAGED_TRUE = 1;
    public static final Integer IS_PAGED_FALSE = 0;

    private FinanceConstant(){
        throw new IllegalStateException("Utility class");
    }
}
