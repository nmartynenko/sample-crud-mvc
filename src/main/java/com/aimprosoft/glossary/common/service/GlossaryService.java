package com.aimprosoft.glossary.common.service;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface GlossaryService extends BaseCrudService<Glossary> {
}
