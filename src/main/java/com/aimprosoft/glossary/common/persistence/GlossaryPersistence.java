package com.aimprosoft.glossary.common.persistence;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface GlossaryPersistence extends JpaRepository<Glossary, Long> {
}