package com.jt.aspect;


import com.jt.vo.SysResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class SysResultAspect {
    @ExceptionHandler({RuntimeException.class,})
    public SysResult sysResultFail(Exception e){
        e.printStackTrace();
        log.error(e.getMessage());
        return SysResult.fail();
    }
}
