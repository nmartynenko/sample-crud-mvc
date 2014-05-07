package com.aimprosoft.glossary.common.persistence;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlossaryPersistence extends JpaRepository<Glossary, Long> {
}