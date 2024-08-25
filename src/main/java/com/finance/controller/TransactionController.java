package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.transaction.ResponseListMyTransactionDto;
import com.finance.dto.transaction.TransactionAdminDto;
import com.finance.form.transaction.ApproveTransactionForm;
import com.finance.form.transaction.CreateTransactionForm;
import com.finance.form.transaction.RejectTransactionForm;
import com.finance.form.transaction.UpdateTransactionForm;
import com.finance.mapper.TransactionMapper;
import com.finance.model.Category;
import com.finance.model.Transaction;
import com.finance.model.TransactionGroup;
import com.finance.model.criteria.MyTransactionCriteria;
import com.finance.model.criteria.TransactionCriteria;
import com.finance.repository.CategoryRepository;
import com.finance.repository.TransactionGroupRepository;
import com.finance.repository.TransactionRepository;
import com.finance.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class TransactionController extends ABasicController{
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionGroupRepository transactionGroupRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_L')")
    public ApiMessageDto<ResponseListDto<List<TransactionAdminDto>>> list(TransactionCriteria transactionCriteria, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findAll(transactionCriteria.getCriteria(), pageable);
        ResponseListDto<List<TransactionAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(transactionMapper.fromEntityListToTransactionAdminDtoList(transactions.getContent()));
        responseListObj.setTotalPages(transactions.getTotalPages());
        responseListObj.setTotalElements(transactions.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction success");
    }
    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_V')")
    public ApiMessageDto<TransactionAdminDto> get(@PathVariable Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if (transaction == null) {
            makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_FOUND, "Not found transaction");
        }
        return makeSuccessResponse(transactionMapper.fromEntityToTransactionAdminDto(transaction), "Get transaction success");
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListDto<List<TransactionAdminDto>>> autoComplete(TransactionCriteria transactionCriteria) {
        Pageable pageable = PageRequest.of(0,10);
        transactionCriteria.setStatus(FinanceConstant.STATUS_ACTIVE);
        Page<Transaction> transactions = transactionRepository.findAll(transactionCriteria.getCriteria(), pageable);
        ResponseListDto<List<TransactionAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(transactionMapper.fromEntityListToTransactionAdminDtoList(transactions.getContent()));
        responseListObj.setTotalPages(transactions.getTotalPages());
        responseListObj.setTotalElements(transactions.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list transaction success");
    }

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_C')")
    public ApiMessageDto<String> create(@Valid @RequestBody CreateTransactionForm createTransactionForm, BindingResult bindingResult) {
        Transaction transaction = transactionMapper.fromCreateTransactionFormToEntity(createTransactionForm);
        if (createTransactionForm.getTransactionGroupId() != null) {
            TransactionGroup transactionGroup = transactionGroupRepository.findById(createTransactionForm.getTransactionGroupId()).orElse(null);
            if (transactionGroup == null){
                return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NOT_FOUND, "Not found transaction group");
            }
            transaction.setTransactionGroup(transactionGroup);
        }
        if (createTransactionForm.getCategoryId() != null) {
            Category category = categoryRepository.findById(createTransactionForm.getCategoryId()).orElse(null);
            if (category == null) {
                return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
            }
            if (!createTransactionForm.getKind().equals(category.getKind())) {
                return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_KIND_INVALID, "Transaction kind not match category");
            }
            transaction.setCategory(category);
        }

        transaction.setState(FinanceConstant.TRANSACTION_STATE_CREATED);
        transactionRepository.save(transaction);
        return makeSuccessResponse(null, "Create transaction success");
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_U')")
    public ApiMessageDto<String> update(@Valid @RequestBody UpdateTransactionForm updateTransactionForm, BindingResult bindingResult) {
        Transaction transaction = transactionRepository.findById(updateTransactionForm.getId()).orElse(null);
        if (transaction == null) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_FOUND, "Not found transaction");
        }
        if (transaction.getState().equals(FinanceConstant.TRANSACTION_STATE_PAID)) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_ALLOW_UPDATE, "Not allow to update transaction");

        }
        if (updateTransactionForm.getTransactionGroupId() != null) {
            TransactionGroup transactionGroup = transactionGroupRepository.findById(updateTransactionForm.getTransactionGroupId()).orElse(null);
            if (transactionGroup == null){
                return makeErrorResponse(ErrorCode.TRANSACTION_GROUP_ERROR_NOT_FOUND, "Not found transaction group");
            }
            transaction.setTransactionGroup(transactionGroup);
        }else {
            transaction.setTransactionGroup(null);
        }
        if (updateTransactionForm.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateTransactionForm.getCategoryId()).orElse(null);
            if (category == null) {
                return makeErrorResponse(ErrorCode.CATEGORY_ERROR_NOT_FOUND, "Not found category");
            }
            if (!updateTransactionForm.getKind().equals(category.getKind())) {
                return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_KIND_INVALID, "Transaction kind not match category");
            }
            transaction.setCategory(category);
        } else {
            transaction.setCategory(null);
        }
        transactionMapper.fromUpdateTransactionFormToEntity(updateTransactionForm, transaction);
        transactionRepository.save(transaction);
        return makeSuccessResponse(null, "Update transaction success");
    }

    @Transactional
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if(transaction == null){
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        transactionRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete department success");
    }
    @GetMapping(value = "/my-transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<ResponseListMyTransactionDto<List<TransactionAdminDto>>> myTransaction(MyTransactionCriteria myTransactionCriteria) {
        List<Transaction> transactions = transactionRepository.findAll(myTransactionCriteria.getCriteria());
        double totalIncome = 0.0;
        double totalExpenditure = 0.0;
        for (Transaction transaction : transactions) {
            String money = transaction.getMoney();
            double amount = Double.parseDouble(money);
            if (transaction.getKind().equals(FinanceConstant.TRANSACTION_KIND_INCOME)) {
                totalIncome += amount;
            } else {
                totalExpenditure += amount;
            }
        }
        List<TransactionAdminDto> transactionDtos = transactionMapper.fromEntityListToTransactionAdminDtoList(transactions);
        String encryptedTotalIncome = ConvertUtils.convertDoubleToString(totalIncome);
        String encryptedTotalExpenditure = ConvertUtils.convertDoubleToString(totalExpenditure);
        ResponseListMyTransactionDto<List<TransactionAdminDto>> responseListObj = new ResponseListMyTransactionDto<>();
        responseListObj.setContent(transactionDtos);
        responseListObj.setTotalIncome(encryptedTotalIncome);
        responseListObj.setTotalExpenditure(encryptedTotalExpenditure);
        return makeSuccessResponse(responseListObj, "Get my transactions success");
    }
    @PutMapping(value = "/approve", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_A')")
    public ApiMessageDto<String> approve(@Valid @RequestBody ApproveTransactionForm approveTransactionForm, BindingResult bindingResult) {
        Transaction transaction = transactionRepository.findById(approveTransactionForm.getId()).orElse(null);
        if (transaction == null) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_FOUND, "Not found transaction");
        }
        if (transaction.getState().equals(FinanceConstant.TRANSACTION_STATE_APPROVE)) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_ALLOW_UPDATE, "Transaction has already been approved");
        }
        if (transaction.getState().equals(FinanceConstant.TRANSACTION_STATE_PAID)) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_ALLOW_UPDATE, "Not allowed to approve transaction");
        }
        transaction.setState(FinanceConstant.TRANSACTION_STATE_APPROVE);
        transactionRepository.save(transaction);
        return makeSuccessResponse(null, "Approve transaction success");
    }
    @PutMapping(value = "/reject", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('TR_R')")
    public ApiMessageDto<String> reject(@Valid @RequestBody RejectTransactionForm rejectTransactionForm, BindingResult bindingResult) {
        Transaction transaction = transactionRepository.findById(rejectTransactionForm.getId()).orElse(null);
        if (transaction == null) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_FOUND, "Not found transaction");
        }
        if (transaction.getState().equals(FinanceConstant.TRANSACTION_STATE_REJECT)) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_ALLOW_UPDATE, "Transaction has already been rejected");
        }
        if (transaction.getState().equals(FinanceConstant.TRANSACTION_STATE_PAID)) {
            return makeErrorResponse(ErrorCode.TRANSACTION_ERROR_NOT_ALLOW_UPDATE, "Not allowed to reject transaction");
        }
        Long paymentPeriodId = transaction.getPaymentPeriod() != null ? transaction.getPaymentPeriod().getId() : 1L;
        transaction.setState(FinanceConstant.TRANSACTION_STATE_REJECT);
        transaction.setPaymentPeriod(null);
        transactionRepository.save(transaction);
        return makeSuccessResponse(null, "Reject transaction success");
    }
}
