package com.aimprosoft.glossary.common.service.impl;

import com.aimprosoft.glossary.common.exception.GlossaryException;
import com.aimprosoft.glossary.common.exception.NoGlossaryFoundException;
import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.aimprosoft.glossary.common.persistence.GlossaryPersistence;
import com.aimprosoft.glossary.common.service.GlossaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class GlossaryServiceImpl extends BaseCrudServiceImpl<Glossary, GlossaryPersistence> implements GlossaryService {

    public Page<Glossary> getCurrentPage(int startRow, int pageSize) throws GlossaryException {
        try {
            return super.getCurrentPage(startRow, pageSize);
        } catch (RuntimeException e) {
            throw new GlossaryException(e);
        }
    }

    @Override
    public Glossary getById(Long glossaryId) throws GlossaryException {
        Glossary glossary = persistence.findOne(glossaryId);

        if (glossary == null) {
            throw new NoGlossaryFoundException(glossaryId);
        }

        return glossary;
    }

    @Override
    public void add(Glossary glossary) throws GlossaryException {
        try {
            super.add(glossary);
        } catch (RuntimeException e) {
            throw new GlossaryException(e);
        }
    }

    @Override
    public void update(Glossary glossary) throws GlossaryException {
        try {
            super.update(glossary);
        } catch (EmptyResultDataAccessException e) {
            throw new NoGlossaryFoundException(e, glossary.getId());
        } catch (RuntimeException e) {
            throw new GlossaryException(e);
        }
    }

    @Override
    public void remove(Glossary glossary) throws GlossaryException {
        try {
            super.remove(glossary);
        } catch (EmptyResultDataAccessException e) {
            throw new NoGlossaryFoundException(e, glossary.getId());
        } catch (RuntimeException e) {
            throw new GlossaryException(e);
        }
    }

    @Override
    public void removeById(Long glossaryId) throws GlossaryException {
        try {
            super.removeById(glossaryId);
        } catch (EmptyResultDataAccessException e) {
            throw new NoGlossaryFoundException(e, glossaryId);
        } catch (RuntimeException e) {
            throw new GlossaryException(e);
        }
    }
}
