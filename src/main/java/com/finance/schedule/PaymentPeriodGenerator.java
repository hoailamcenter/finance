package com.finance.schedule;

import com.finance.constant.FinanceConstant;
import com.finance.model.PaymentPeriod;
import com.finance.repository.PaymentPeriodRepository;
import com.finance.repository.TransactionRepository;
import com.finance.service.FinanceApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Slf4j
public class PaymentPeriodGenerator {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;
    @Autowired
    private FinanceApiService financeApiService;

    @Scheduled(cron = "0 0 0 1 * *", zone = "Asia/Ho_Chi_Minh")
    public void createPaymentPeriod(){
        ZoneId vnZoneId = ZoneId.of("Asia/Ho_Chi_Minh");
        ZonedDateTime nowInVN = ZonedDateTime.now(vnZoneId);
        LocalDate firstDayOfPreviousMonth = nowInVN.toLocalDate().minusMonths(1).withDayOfMonth(1);
        LocalDate lastDayOfPreviousMonth = nowInVN.toLocalDate().minusMonths(1).withDayOfMonth(nowInVN.toLocalDate().minusMonths(1).lengthOfMonth());
        ZonedDateTime startDateVN = firstDayOfPreviousMonth.atStartOfDay(vnZoneId);
        ZonedDateTime endDateVN = lastDayOfPreviousMonth.atTime(23, 59, 59).atZone(vnZoneId);
        ZonedDateTime startDateUTC = startDateVN.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime endDateUTC = endDateVN.withZoneSameInstant(ZoneId.of("UTC"));
        Date startDate = Date.from(startDateUTC.toInstant());
        Date endDate = Date.from(endDateUTC.toInstant());
        PaymentPeriod paymentPeriod = new PaymentPeriod();
        paymentPeriod.setName(new SimpleDateFormat("MM/yyyy").format(endDate));
        paymentPeriod.setState(FinanceConstant.PAYMENT_PERIOD_STATE_CREATED);
        paymentPeriod.setStartDate(startDate);
        paymentPeriod.setEndDate(endDate);
        paymentPeriodRepository.save(paymentPeriod);
        transactionRepository.updateAllByStateAndCreatedDate(paymentPeriod.getId(), FinanceConstant.TRANSACTION_STATE_APPROVE, paymentPeriod.getStartDate(), paymentPeriod.getEndDate());
        financeApiService.recalculatePaymentPeriod(paymentPeriod.getId());
    }
}
