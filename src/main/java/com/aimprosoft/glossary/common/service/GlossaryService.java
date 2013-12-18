package com.aimprosoft.glossary.common.service;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.springframework.data.domain.Page;

public interface GlossaryService {

    Page<Glossary> getCurrentPage(int page, int pageSize) throws GlossaryException;

    Glossary getGlossaryById(long glossaryId) throws GlossaryException;

    void addGlossary(Glossary glossary) throws GlossaryException;

    void updateGlossary(Glossary glossary) throws GlossaryException;

    void removeGlossary(Glossary glossary) throws GlossaryException;

    void removeGlossaryById(long glossaryId) throws GlossaryException;

}
