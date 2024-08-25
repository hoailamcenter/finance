package com.finance.config;

import com.finance.exception.BadRequestException;
import com.finance.exception.ForbiddenException;
import com.finance.exception.NotFoundException;
import com.finance.exception.UnauthorizationException;
import feign.Request;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        Request request = response.request();
        String message = response.body() != null ? response.body().toString() : null;
        switch (response.status()) {
            case 401:
                throw new UnauthorizationException("No access to [" + request.httpMethod() + "] " + request.url());
            case 403:
                throw new ForbiddenException("No access to [" + request.httpMethod() + "] " + request.url());
            case 400:
                throw new BadRequestException(message);
            case 404:
                throw new NotFoundException("Not found");
            default:
                throw new RuntimeException(message);
        }
    }
}
