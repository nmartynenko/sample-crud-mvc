package com.aimprosoft.glossary.servlet.controller;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.exception.NoGlossaryFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlossaryControllerAdvice extends BaseController {

    //EXCEPTION HANDLERS
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {NoGlossaryFoundException.class})
    @ResponseBody
    public String handleNoGlossaryFoundException(NoGlossaryFoundException e){
        return messageSource.getMessage("sample.error.glossary.not.found", new Object[]{e.getModelId()}, getLocale());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {GlossaryException.class})
    @ResponseBody
    public String handleGlossaryException(GlossaryException e){
        return simpleExceptionHandler(e);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseBody
    public String handleAccessDeniedException(AccessDeniedException e){
        return simpleExceptionHandler(e);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseBody
    public String handleAuthenticationException(AuthenticationException e){
        return simpleExceptionHandler(e);
    }

    //do not pass validation
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public Object handleMethodArgumentNotValidException (MethodArgumentNotValidException  e){
        return transformErrors(e.getBindingResult());
    }

    private Map<String, String> transformErrors(Errors errors) {
        Map<String, String> errorsMap = new HashMap<String, String>();

        List<ObjectError> allErrors = errors.getAllErrors();

        for (ObjectError error: allErrors){
            String objectName = (error instanceof FieldError)
                    ? ((FieldError)error).getField()
                    : error.getObjectName();
            errorsMap.put(objectName,
                    messageSource.getMessage(error.getDefaultMessage(), new Object[]{objectName}, getLocale()));
        }

        return errorsMap;
    }
}
