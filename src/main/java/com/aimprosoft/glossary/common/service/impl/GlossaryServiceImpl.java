package com.aimprosoft.glossary.common.service.impl;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.aimprosoft.glossary.common.persistence.GlossaryPersistence;
import com.aimprosoft.glossary.common.service.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GlossaryServiceImpl implements GlossaryService{

    @Autowired
    private GlossaryPersistence glossaryPersistence;

    public Page<Glossary> getCurrentPage(int startRow, int pageSize) {
        Pageable pageable = null;
        if (startRow >= 0 && pageSize > 0){
            pageable = new PageRequest(startRow / pageSize, pageSize);
        }
        return glossaryPersistence.findAll(pageable);
    }

    @Override
    public Glossary getGlossaryById(long glossaryId) throws GlossaryException {
        return glossaryPersistence.findOne(glossaryId);
    }

    @Override
    public void addGlossary(Glossary glossary) throws GlossaryException {
        glossaryPersistence.save(glossary);
    }

    @Override
    public void updateGlossary(Glossary glossary) throws GlossaryException {
        glossaryPersistence.save(glossary);
    }

    @Override
    public void removeGlossary(Glossary glossary) throws GlossaryException {
        glossaryPersistence.delete(glossary);
    }

    @Override
    public void removeGlossaryById(long glossaryId) throws GlossaryException {
        glossaryPersistence.delete(glossaryId);
    }
}
