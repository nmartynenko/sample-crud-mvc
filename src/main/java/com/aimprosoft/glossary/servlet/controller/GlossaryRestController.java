package com.aimprosoft.glossary.servlet.controller;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.aimprosoft.glossary.common.persistence.UserPersistence;
import com.aimprosoft.glossary.common.service.GlossaryService;
import com.aimprosoft.glossary.servlet.model.GlossaryList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Glossaries REST-controller. It produces and consumes JSON. For "USER" role all actions are read-only.
 *
 * @see com.aimprosoft.glossary.common.service.GlossaryService
 */
@RestController
public class GlossaryRestController extends BaseController {

    @Autowired
    protected GlossaryService glossaryService;

    @Autowired
    protected UserPersistence userPersistence;

    @RequestMapping(value = "/glossaries",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object getGlossaries(@RequestParam(value = "startRow", required = false, defaultValue = "0") Integer startRow,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "0") Integer pageSize)
            throws GlossaryException {
        return new GlossaryList(glossaryService.getCurrentPage(startRow, pageSize));
    }

    @RequestMapping(value = "/glossaries/{id}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Object getGlossary(@PathVariable Long id) throws GlossaryException {
        return glossaryService.getById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/glossaries",
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void saveGlossary(@RequestBody @Validated Glossary glossary) throws GlossaryException {
        glossaryService.add(glossary);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/glossaries",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public void updateGlossary(@RequestBody @Validated Glossary glossary) throws GlossaryException {
        glossaryService.update(glossary);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/glossaries/{glossaryId}",
            method = RequestMethod.DELETE)
    public void removeGlossary(@PathVariable("glossaryId") Long glossaryId) throws GlossaryException {
        glossaryService.removeById(glossaryId);
    }
}
