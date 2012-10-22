package com.aimprosoft.glossary.common.model.impl;

import com.aimprosoft.glossary.common.model.BusinessModel;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "glossary")
public class Glossary extends BusinessModel {

    //validation
    @NotNull(message = "sample.error.not.null")
    @NotEmpty(message = "sample.error.not.empty")
    //hibernate
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    //validation
    @NotNull(message = "sample.error.not.null")
    @NotEmpty(message = "sample.error.not.empty")
    //hibernate
    @Lob
    @Column(name = "description", nullable = true)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
