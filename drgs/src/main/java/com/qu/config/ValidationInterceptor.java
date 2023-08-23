package com.qu.config;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ValidationInterceptor extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleBindException(
            BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        status=HttpStatus.OK;
        return new ResponseEntity<>(getError(ex.getBindingResult().getAllErrors()) , status);
    }


    /**
     * 解决 JSON 请求统一返回参数
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        status=HttpStatus.OK;
        return new ResponseEntity<>(getError(ex.getBindingResult().getAllErrors()) , status);
    }

    private Result getError(List<ObjectError> allErrors) {
        StringBuffer message = new StringBuffer();
        for(ObjectError error: allErrors){
            message.append(error.getDefaultMessage()).append(" & ");
        }
        log.error(message.substring(0, message.length() - 3));  // 因为&两边空格
        return Result.error(message.substring(0, message.length() - 3));
    }
}