package com.aimprosoft.glossary.common.service;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

public interface GlossaryService {

    Page<Glossary> getCurrentPage(int page, int pageSize);

    Glossary getGlossaryById(long glossaryId) throws GlossaryException;

    @PreAuthorize("hasRole('ADMIN')")
    void addGlossary(Glossary glossary) throws GlossaryException;

    @PreAuthorize("hasRole('ADMIN')")
    void updateGlossary(Glossary glossary) throws GlossaryException;

    @PreAuthorize("hasRole('ADMIN')")
    void removeGlossary(Glossary glossary) throws GlossaryException;

    @PreAuthorize("hasRole('ADMIN')")
    void removeGlossaryById(long glossaryId) throws GlossaryException;

}
