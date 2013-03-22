package com.aimprosoft.glossary.servlet.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public abstract class BaseController {

    protected Logger _logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected MessageSource messageSource;

    protected HttpServletRequest getRequest() {
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    //logs exception and returns it's message
    protected String simpleExceptionHandler(Throwable th){
        _logger.error(th.getMessage(), th);
        return th.getMessage();
    }

}
