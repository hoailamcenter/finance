package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.paymentPeriod.PaymentPeriodAdminDto;
import com.finance.dto.paymentPeriod.PaymentPeriodDto;
import com.finance.form.paymentPeriod.ApprovePaymentPeriodForm;
import com.finance.form.paymentPeriod.CreatePaymentPeriodForm;
import com.finance.form.paymentPeriod.RecalculatePaymentPeriodForm;
import com.finance.mapper.PaymentPeriodMapper;
import com.finance.mapper.TransactionMapper;
import com.finance.model.PaymentPeriod;
import com.finance.model.Transaction;
import com.finance.model.criteria.PaymentPeriodCriteria;
import com.finance.repository.PaymentPeriodRepository;
import com.finance.repository.TransactionRepository;
import com.finance.service.FinanceApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/payment-period")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class PaymentPeriodController extends ABasicController {
    @Autowired
    private PaymentPeriodRepository paymentPeriodRepository;
    @Autowired
    private PaymentPeriodMapper paymentPeriodMapper;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private FinanceApiService financeApiService;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PA_P_V')")
    public ApiMessageDto<PaymentPeriodAdminDto> get(@PathVariable("id") Long id) {
        PaymentPeriod paymentPeriod = paymentPeriodRepository.findById(id).orElse(null);
        if (paymentPeriod == null) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_NOT_FOUND, "Not found payment period");
        }
        PaymentPeriodAdminDto paymentPeriodAdminDto = paymentPeriodMapper.fromEntityToPaymentPeriodAdminDto(paymentPeriod);
        List<Transaction> transactions = transactionRepository.findAllByPaymentPeriodId(id);
        paymentPeriodAdminDto.setTransactions(transactionMapper.fromEntityListToTransactionDtoListForPaymentPeriod(transactions));
        return makeSuccessResponse(paymentPeriodAdminDto, "Get payment period success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PA_P_L')")
    public ApiMessageDto<ResponseListDto<List<PaymentPeriodAdminDto>>> list(PaymentPeriodCriteria paymentPeriodCriteria, Pageable pageable) {
        if (paymentPeriodCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_FALSE)){
            pageable = PageRequest.of(0, Integer.MAX_VALUE);
        }
        Page<PaymentPeriod> paymentPeriods = paymentPeriodRepository.findAll(paymentPeriodCriteria.getCriteria(), pageable);
        ResponseListDto<List<PaymentPeriodAdminDto>> responseListObj = new ResponseListDto<>();
        List<PaymentPeriodAdminDto> paymentPeriodAdminDtos = paymentPeriodMapper.fromEntityListToPaymentPeriodAdminDtoList(paymentPeriods.getContent());
        responseListObj.setContent(paymentPeriodAdminDtos);
        responseListObj.setTotalPages(paymentPeriods.getTotalPages());
        responseListObj.setTotalElements(paymentPeriods.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list payment period success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<PaymentPeriodDto>>> autoComplete(PaymentPeriodCriteria paymentPeriodCriteria) {
        Pageable pageable = paymentPeriodCriteria.getIsPaged().equals(FinanceConstant.IS_PAGED_TRUE) ? PageRequest.of(0, 10) : PageRequest.of(0, Integer.MAX_VALUE);
        paymentPeriodCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<PaymentPeriod> paymentPeriods = paymentPeriodRepository.findAll(paymentPeriodCriteria.getCriteria(), pageable);
        ResponseListDto<List<PaymentPeriodDto>> responseListObj = new ResponseListDto<>();
        List<PaymentPeriodDto> paymentPeriodDtos = paymentPeriodMapper.fromEntityListToPaymentPeriodDtoList(paymentPeriods.getContent());
        responseListObj.setContent(paymentPeriodDtos);
        responseListObj.setTotalPages(paymentPeriods.getTotalPages());
        responseListObj.setTotalElements(paymentPeriods.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list payment period success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PA_P_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreatePaymentPeriodForm createPaymentPeriodForm, BindingResult bindingResult) {
        if (createPaymentPeriodForm.getStartDate().after(createPaymentPeriodForm.getEndDate())) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_DATE_INVALID, "Start date must be before end date");
        }
        PaymentPeriod paymentPeriod = paymentPeriodMapper.fromCreatePaymentPeriodFormToEntity(createPaymentPeriodForm);
        paymentPeriod.setState(FinanceConstant.PAYMENT_PERIOD_STATE_CREATED);
        paymentPeriodRepository.save(paymentPeriod);
        transactionRepository.updateAllByStateAndCreatedDate(paymentPeriod.getId(), FinanceConstant.TRANSACTION_STATE_APPROVE, paymentPeriod.getStartDate(), paymentPeriod.getEndDate());
        financeApiService.recalculatePaymentPeriod(paymentPeriod.getId());
        return makeSuccessResponse(null, "Create payment period success");
    }

    @PutMapping(value = "/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PA_P_A')")
    public ApiMessageDto<String> changeState(@Valid @RequestBody ApprovePaymentPeriodForm approvePaymentPeriodForm, BindingResult bindingResult) {
        PaymentPeriod paymentPeriod = paymentPeriodRepository.findById(approvePaymentPeriodForm.getId()).orElse(null);
        if (paymentPeriod == null) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_NOT_FOUND, "Not found payment period");
        }
        if (paymentPeriod.getState().equals(FinanceConstant.PAYMENT_PERIOD_STATE_APPROVE)) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_NOT_ALLOW_UPDATE, "Payment period has already been approved");
        }
        paymentPeriod.setState(FinanceConstant.PAYMENT_PERIOD_STATE_APPROVE);
        paymentPeriodRepository.save(paymentPeriod);
        transactionRepository.updateStateAllByPaymentPeriodId(paymentPeriod.getId(), FinanceConstant.TRANSACTION_STATE_PAID);
        List<Transaction> transactions = transactionRepository.findAllByPaymentPeriodId(paymentPeriod.getId());
        transactions.forEach(transaction -> financeApiService.createTransactionHistory(getCurrentUser(), transaction, approvePaymentPeriodForm.getNote()));
        return makeSuccessResponse(null, "Change state payment period success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PA_P_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        PaymentPeriod paymentPeriod = paymentPeriodRepository.findById(id).orElse(null);
        if (paymentPeriod == null) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_NOT_FOUND, "Not found payment period");
        }
        transactionRepository.updateAllByPaymentPeriodId(id);
        paymentPeriodRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete payment period success");
    }

    @PutMapping(value = "/recalculate", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('PA_P_R')")
    public ApiMessageDto<String> recalculate(@Valid @RequestBody RecalculatePaymentPeriodForm recalculatePaymentPeriodForm, BindingResult bindingResult) {
        PaymentPeriod paymentPeriod = paymentPeriodRepository.findById(recalculatePaymentPeriodForm.getId()).orElse(null);
        if (paymentPeriod == null) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_NOT_FOUND, "Not found payment period");
        }
        if (paymentPeriod.getState().equals(FinanceConstant.PAYMENT_PERIOD_STATE_APPROVE)) {
            return makeErrorResponse(ErrorCode.PAYMENT_PERIOD_ERROR_NOT_ALLOW_RECALCULATE, "Payment period has already been approved");
        }
        transactionRepository.updateAllByPaymentPeriodId(paymentPeriod.getId());
        transactionRepository.updateAllByStateAndCreatedDate(paymentPeriod.getId(), FinanceConstant.TRANSACTION_STATE_APPROVE, paymentPeriod.getStartDate(), paymentPeriod.getEndDate());
        financeApiService.recalculatePaymentPeriod(paymentPeriod.getId());
        return makeSuccessResponse(null, "Recalculate payment period success");
    }
}
