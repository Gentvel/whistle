package com.whistle.starter.response;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.whistle.starter.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Gentvel
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class WhistleGlobalExceptionHandler {



    @ExceptionHandler(BindException.class)
    public Result<Object> bindException(BindException e){
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return Result.fail(message);
    }


    @ExceptionHandler(BizException.class)
    public Result<Object> bizException(BizException e) {
        log.info(e.toString());
        return Result.fail(e);
    }

    @ExceptionHandler(MybatisPlusException.class)
    public Result<Object> mybatisPlusException(MybatisPlusException e) {
        log.info("",e);
        return Result.fail(ResponseInterface.DATA_ERROR,e.getMessage());
    }

}
