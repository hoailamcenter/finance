package com.finance.mapper;


import com.finance.dto.paymentPeriod.PaymentPeriodAdminDto;
import com.finance.dto.paymentPeriod.PaymentPeriodDto;
import com.finance.form.paymentPeriod.CreatePaymentPeriodForm;
import com.finance.form.paymentPeriod.UpdatePaymentPeriodForm;
import com.finance.model.PaymentPeriod;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PaymentPeriodMapper {
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    PaymentPeriod fromCreatePaymentPeriodFormToEntity(CreatePaymentPeriodForm createPaymentPeriodForm);

    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "name", target = "name")
    @BeanMapping(ignoreByDefault = true)
    void fromUpdatePaymentPeriodFormToEntity(UpdatePaymentPeriodForm updatePaymentPeriodForm, @MappingTarget PaymentPeriod paymentPeriod);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "totalIncome", target = "totalIncome")
    @Mapping(source = "totalExpenditure", target = "totalExpenditure")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToPaymentPeriodAdminDto")
    PaymentPeriodAdminDto fromEntityToPaymentPeriodAdminDto(PaymentPeriod paymentPeriod);

    @IterableMapping(elementTargetType = PaymentPeriodAdminDto.class, qualifiedByName = "fromEntityToPaymentPeriodAdminDto")
    List<PaymentPeriodAdminDto> fromEntityListToPaymentPeriodAdminDtoList(List<PaymentPeriod> paymentPeriods);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "state", target = "state")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "totalIncome", target = "totalIncome")
    @Mapping(source = "totalExpenditure", target = "totalExpenditure")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromEntityToPaymentPeriodDto")
    PaymentPeriodDto fromEntityToPaymentPeriodDto(PaymentPeriod paymentPeriod);

    @IterableMapping(elementTargetType = PaymentPeriodDto.class, qualifiedByName = "fromEntityToPaymentPeriodDto")
    List<PaymentPeriodDto> fromEntityListToPaymentPeriodDtoList(List<PaymentPeriod> paymentPeriods);
}
