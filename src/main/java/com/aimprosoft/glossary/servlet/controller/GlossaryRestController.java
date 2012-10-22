package com.aimprosoft.glossary.servlet.controller;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.aimprosoft.glossary.servlet.model.GlossaryList;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Glossaries REST-controller. It produces and consumes JSON. For "USER" role all actions are read-only.
 *
 * @see com.aimprosoft.glossary.common.service.GlossaryService
 */
@Controller
public class GlossaryRestController extends BaseController {

    @RequestMapping(value = "/glossaries",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Object getGlossaries(@RequestParam(value = "startRow", required = false, defaultValue = "0") Integer startRow,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "0") Integer pageSize)
            throws GlossaryException {
        return new GlossaryList(glossaryService.getCurrentPage(startRow, pageSize));
    }

    @RequestMapping(value = "/glossaries/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Object getGlossary(@PathVariable Long id) throws GlossaryException {
        return glossaryService.getGlossaryById(id);
    }

    @RequestMapping(value = "/glossaries",
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public void saveGlossary(@RequestBody @Validated Glossary glossary) throws GlossaryException {
        glossaryService.addGlossary(glossary);
    }

    @RequestMapping(value = "/glossaries",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public void updateGlossary(@RequestBody @Validated Glossary glossary) throws GlossaryException {
        glossaryService.updateGlossary(glossary);
    }

    @RequestMapping(value = "/glossaries",
            /*method = RequestMethod.DELETE)*/
            //only GET, POST, PUT allowed
            method = RequestMethod.POST)
    @ResponseBody
    public void removeGlossary(@RequestParam("glossaryId") Long glossaryId) throws GlossaryException {
        glossaryService.removeGlossaryById(glossaryId);
    }

    //EXCEPTION HANDLERS

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
            errorsMap.put(error.getObjectName(),
                    messageSource.getMessage(error.getDefaultMessage(), new Object[]{error.getObjectName()}, getLocale()));
        }

        return errorsMap;
    }

}
