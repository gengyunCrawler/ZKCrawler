package cn.com.cloudpioneer.zkcrawlerAPI.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;

/**
 * <类详细说明：异常收集>
 *
 * @Author： Huanghai
 * @Version: 2016-11-15
 **/
@ControllerAdvice(annotations = {RestController.class})
public class ExceptionReaper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionReaper.class);

    @ExceptionHandler(value = { IOException.class , RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String  exception(Exception exception, WebRequest request) {
        LOGGER.info("Catch an exception", exception);
        return  "test 500 错误！";
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String noMapping(Exception exception, WebRequest request) {
        LOGGER.info("No mapping exception", exception);
        return  "test 400 错误";
    }
}
