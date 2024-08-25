package com.finance.controller;

import com.finance.constant.FinanceConstant;
import com.finance.form.account.*;
import com.finance.mapper.AccountMapper;
import com.finance.model.Account;
import com.finance.model.Department;
import com.finance.repository.*;
import com.finance.service.FinanceApiService;
import com.finance.utils.*;
import com.finance.dto.ApiMessageDto;
import com.finance.dto.ErrorCode;
import com.finance.dto.ResponseListDto;
import com.finance.dto.account.AccountAdminDto;
import com.finance.dto.account.AccountDto;
import com.finance.dto.account.AccountForgetPasswordDto;
import com.finance.model.Group;
import com.finance.model.criteria.AccountCriteria;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/v1/account")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class AccountController extends ABasicController {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private KeyInformationRepository keyInformationRepository;
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;
    @Autowired
    private UserGroupNotificationRepository userGroupNotificationRepository;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private FinanceApiService financeApiService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_V')")
    public ApiMessageDto<AccountAdminDto> get(@PathVariable("id") Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        return makeSuccessResponse(accountMapper.fromEntityToAccountAdminDto(account), "Get account success");
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_L')")
    public ApiMessageDto<ResponseListDto<List<AccountAdminDto>>> list(AccountCriteria accountCriteria, Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(accountCriteria.getCriteria(), pageable);
        ResponseListDto<List<AccountAdminDto>> responseListObj = new ResponseListDto<>();
        responseListObj.setContent(accountMapper.fromEntityListToAccountAdminDtoList(accounts.getContent()));
        responseListObj.setTotalPages(accounts.getTotalPages());
        responseListObj.setTotalElements(accounts.getTotalElements());
        return makeSuccessResponse(responseListObj, "Get list account success");
    }

    @PostMapping(value = "/create-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_C_AD')")
    public ApiMessageDto<String> createAdmin(@Valid @RequestBody CreateAccountAdminForm createAccountAdminForm, BindingResult bindingResult) {
        Account accountByUsername = accountRepository.findFirstByUsername(createAccountAdminForm.getUsername()).orElse(null);
        if (accountByUsername != null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_USERNAME_EXISTED, "Username existed");
        }
        Account accountByEmail = accountRepository.findFirstByEmail(createAccountAdminForm.getEmail()).orElse(null);
        if (accountByEmail != null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED, "Email existed");
        }
        Account accountByPhone = accountRepository.findFirstByPhone(createAccountAdminForm.getPhone()).orElse(null);
        if (accountByPhone != null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_PHONE_EXISTED, "Phone existed");
        }
        if (createAccountAdminForm.getBirthDate() != null && createAccountAdminForm.getBirthDate().after(new Date())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_BIRTHDATE_INVALID, "Birthdate is invalid");
        }
        Group group = groupRepository.findById(createAccountAdminForm.getGroupId()).orElse(null);
        if (group == null) {
            return makeErrorResponse(ErrorCode.GROUP_ERROR_NOT_FOUND, "Not found group");
        }
        Department department = departmentRepository.findById(createAccountAdminForm.getDepartmentId()).orElse(null);
        if (department == null) {
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        Account account = accountMapper.fromCreateAccountAdminFormToEntity(createAccountAdminForm);
        account.setPassword(passwordEncoder.encode(createAccountAdminForm.getPassword()));
        account.setKind(FinanceConstant.USER_KIND_ADMIN);
        account.setGroup(group);
        account.setDepartment(department);
        accountRepository.save(account);
        return makeSuccessResponse(null, "Create account admin success");
    }

    @PutMapping(value = "/update-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_U_AD')")
    public ApiMessageDto<String> updateAdmin(@Valid @RequestBody UpdateAccountAdminForm updateAccountAdminForm, BindingResult bindingResult) {
        Account account = accountRepository.findById(updateAccountAdminForm.getId()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        if (updateAccountAdminForm.getEmail() != null && !updateAccountAdminForm.getEmail().equals(account.getEmail())) {
            Account accountByEmail = accountRepository.findFirstByEmail(updateAccountAdminForm.getEmail()).orElse(null);
            if (accountByEmail != null) {
                return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_EMAIL_EXISTED, "Email existed");
            }
        }
        if (updateAccountAdminForm.getPhone() != null && !updateAccountAdminForm.getPhone().equals(account.getPhone())) {
            Account accountByPhone = accountRepository.findFirstByPhone(updateAccountAdminForm.getPhone()).orElse(null);
            if (accountByPhone != null) {
                return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_PHONE_EXISTED, "Phone existed");
            }
        }
        Group group = groupRepository.findById(updateAccountAdminForm.getGroupId()).orElse(null);
        if (group == null) {
            return makeErrorResponse(ErrorCode.GROUP_ERROR_NOT_FOUND, "Not found group");
        }
        Department department = departmentRepository.findById(updateAccountAdminForm.getDepartmentId()).orElse(null);
        if (department == null) {
            return makeErrorResponse(ErrorCode.DEPARTMENT_ERROR_NOT_FOUND, "Not found department");
        }
        if (StringUtils.isNoneBlank(updateAccountAdminForm.getAvatarPath())) {
            if (!updateAccountAdminForm.getAvatarPath().equals(account.getAvatarPath())) {
                financeApiService.deleteFile(account.getAvatarPath());
            }
            account.setAvatarPath(updateAccountAdminForm.getAvatarPath());
        }
        if (updateAccountAdminForm.getBirthDate() != null && updateAccountAdminForm.getBirthDate().after(new Date())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_BIRTHDATE_INVALID, "Birthdate is invalid");
        }
        accountMapper.fromUpdateAccountAdminFormToEntity(updateAccountAdminForm, account);
        account.setGroup(group);
        account.setDepartment(department);
        accountRepository.save(account);
        return makeSuccessResponse(null, "Update account admin success");
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ACC_D')")
    public ApiMessageDto<String> delete(@PathVariable("id") Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        if (account.getIsSuperAdmin()) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_ALLOW_DELETE_SUPPER_ADMIN, "Not allow to delete super admin");
        }
        if (Long.valueOf(getCurrentUser()).equals(account.getId())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_ALLOW_DELETE_YOURSELF, "Not allow to delete yourself");
        }
        financeApiService.deleteFile(account.getAvatarPath());
        userGroupNotificationRepository.deleteAllByAccountId(id);
        transactionHistoryRepository.updateAllByAccountId(id);
        keyInformationRepository.updateAllByAccountId(id);
        accountRepository.deleteById(id);
        return makeSuccessResponse(null, "Delete account success");
    }

    @GetMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AccountDto> profile() {
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        return makeSuccessResponse(accountMapper.fromEntityToAccountDto(account), "Get profile success");
    }

    @PutMapping(value = "/update-profile-admin", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> updateProfileAdmin(@Valid @RequestBody UpdateProfileAdminForm updateProfileAdminForm, BindingResult bindingResult) {
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        if (!passwordEncoder.matches(updateProfileAdminForm.getOldPassword(), account.getPassword())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_WRONG_PASSWORD, "Old password is incorrect");
        }
        if (StringUtils.isNoneBlank(updateProfileAdminForm.getAvatarPath())) {
            if (!updateProfileAdminForm.getAvatarPath().equals(account.getAvatarPath())) {
                //delete old image
                financeApiService.deleteFile(account.getAvatarPath());
            }
            account.setAvatarPath(updateProfileAdminForm.getAvatarPath());
        }
        if (updateProfileAdminForm.getBirthDate() != null && updateProfileAdminForm.getBirthDate().after(new Date())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_BIRTHDATE_INVALID, "Birthdate is invalid");
        }
        accountMapper.fromUpdateProfileAdminFormToEntity(updateProfileAdminForm, account);
        accountRepository.save(account);
        return makeSuccessResponse(null, "Update profile success");
    }

    @PostMapping(value = "/request-forget-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<AccountForgetPasswordDto> requestForgetPassword(@Valid @RequestBody RequestForgetPasswordForm forgetForm, BindingResult bindingResult) {
        Account account = accountRepository.findFirstByEmail(forgetForm.getEmail()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        String otp = financeApiService.getOTPForgetPassword();
        account.setAttemptCode(0);
        account.setResetPwdCode(otp);
        account.setResetPwdTime(new Date());
        accountRepository.save(account);
        financeApiService.sendEmail(account.getEmail(), "OTP: " + otp, "Request forget password successful, please check email", false);
        AccountForgetPasswordDto accountForgetPasswordDto = new AccountForgetPasswordDto();
        String zipUserId = ZipUtils.zipString(account.getId() + ";" + otp);
        accountForgetPasswordDto.setUserId(zipUserId);
        return makeSuccessResponse(accountForgetPasswordDto, "Request forget password successful, please check email");
    }

    @PostMapping(value = "/reset-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> forgetPassword(@Valid @RequestBody ResetPasswordForm resetPasswordForm, BindingResult bindingResult) {
        String[] unzip = ZipUtils.unzipString(resetPasswordForm.getUserId()).split(";", 2);
        Long id = ConvertUtils.convertStringToLong(unzip[0]);
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        if (account.getAttemptCode() >= FinanceConstant.MAX_ATTEMPT_FORGET_PWD) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_EXCEEDED_NUMBER_OF_INPUT_ATTEMPT_OTP, "Exceeded number of input attempt OTP");
        }
        if (!account.getResetPwdCode().equals(resetPasswordForm.getOtp()) ||
                (new Date().getTime() - account.getResetPwdTime().getTime() >= FinanceConstant.MAX_TIME_FORGET_PWD)) {
            account.setAttemptCode(account.getAttemptCode() + 1);
            accountRepository.save(account);
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_OTP_INVALID, "OTP code invalid or has expired");
        }
        if (passwordEncoder.matches(resetPasswordForm.getNewPassword(), account.getPassword())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NEW_PASSWORD_INVALID, "New password must be different from old password");
        }
        account.setResetPwdTime(null);
        account.setResetPwdCode(null);
        account.setAttemptCode(null);
        account.setPassword(passwordEncoder.encode(resetPasswordForm.getNewPassword()));
        accountRepository.save(account);
        return makeSuccessResponse(null, "Reset password success");
    }

    @PutMapping(value = "/change-profile-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<String> changeProfilePassword(@Valid @RequestBody ChangeProfilePasswordAccountForm changeProfilePasswordAccountForm, BindingResult bindingResult) {
        Account account = accountRepository.findById(getCurrentUser()).orElse(null);
        if (account == null) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NOT_FOUND, "Not found account");
        }
        if (!passwordEncoder.matches(changeProfilePasswordAccountForm.getOldPassword(), account.getPassword())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_WRONG_PASSWORD, "Old password is incorrect");
        }
        if (changeProfilePasswordAccountForm.getNewPassword().equals(changeProfilePasswordAccountForm.getOldPassword())) {
            return makeErrorResponse(ErrorCode.ACCOUNT_ERROR_NEW_PASSWORD_INVALID, "New password must be different from old password");
        }
        account.setPassword(passwordEncoder.encode(changeProfilePasswordAccountForm.getNewPassword()));
        accountRepository.save(account);
        return makeSuccessResponse(null, "Change profile password success");
    }
}
