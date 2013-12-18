package com.aimprosoft.glossary.common.model.impl;

import com.aimprosoft.glossary.common.model.BusinessModel;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;

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
    @Column(name = "description", nullable = true, length = 4096)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Glossary glossary = (Glossary) o;

        if (id != null ? !id.equals(glossary.id) : glossary.id != null) return false;
        if (description != null ? !description.equals(glossary.description) : glossary.description != null)
            return false;
        if (name != null ? !name.equals(glossary.name) : glossary.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
