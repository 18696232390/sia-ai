package org.sia.handler;

import lombok.extern.slf4j.Slf4j;
import org.sia.exception.BusinessException;
import org.sia.exception.CallBackException;
import org.sia.vo.R;
import org.sia.enums.ResultEnum;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;


/**
 * @Description: 全局异常处理
 * @Author: 高灶顺
 * @CreateDate: 2022/5/30 14:43
 */
@Slf4j
@RestControllerAdvice
public class ExceptionGlobalHandler {
    // 业务异常处理
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public R defaultServiceExcepitonHandler(HttpServletRequest request, BusinessException e) {
        log.error("业务异常 API:{}", request.getRequestURL());
        log.error("错误信息", e);
        return R.builder()
                .code(e.getCode())
                .errMsg(e.getMessage())
                .build();
    }

    // 请求参数绑定异常处理
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.OK)
    public R defaultBindingExcepitonHandler(HttpServletRequest request, BindException e) {
        log.error("参数绑定校验异常 API:{}", request.getRequestURL());
        log.error("错误信息", e);
        return R.builder()
                .code(ResultEnum.REQUEST_PARAM_VALID.getCode())
                .errMsg(String.format(ResultEnum.REQUEST_PARAM_VALID.getMessage()))
                .data(getParamValidErrorMsg(e.getBindingResult()))
                .build();
    }

    // 请求参数绑定异常处理
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    public R defaultBindingExcepitonHandler(HttpServletRequest request, MethodArgumentNotValidException e) {
        log.error("参数绑定校验异常 API:{}", request.getRequestURL());
        log.error("错误信息", e);
        return R.builder()
                .code(ResultEnum.REQUEST_PARAM_VALID.getCode())
                .errMsg(String.format(ResultEnum.REQUEST_PARAM_VALID.getMessage(), getParamValidErrorMsg(e.getBindingResult())))
                .build();
    }

    // 回调异常
    @ExceptionHandler(CallBackException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R defultCbExcepitonHandler(HttpServletRequest request, Exception e) {
        log.error("回调异常 API:{} ", request.getRequestURL());
        log.error("错误信息", e);
        return R.builder()
                .code(ResultEnum.FAIL.getCode())
                .errMsg(String.format(ResultEnum.FAIL.getMessage(), e.getMessage()))
                .build();
    }

    // 未定义异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public R defultExcepitonHandler(HttpServletRequest request, Exception e) {
        log.error("系统异常 API:{} ", request.getRequestURL());
        log.error("错误信息", e);
        return R.builder()
                .code(ResultEnum.FAIL.getCode())
                .errMsg(String.format(ResultEnum.FAIL.getMessage(), e.getMessage()))
                .build();
    }

    private String getParamValidErrorMsg(BindingResult e) {
        return e.getFieldError().getDefaultMessage();
    }
}
