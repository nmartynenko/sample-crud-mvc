package com.aimprosoft.glossary.servlet.model;

import com.aimprosoft.glossary.common.model.impl.Glossary;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.data.domain.Page;

/**
 * This simple wrapper for ignoring some properties of org.springframework.data.domain.Page, f.e. "iterator"
 */
@JsonRootName("result")
@JsonIgnoreProperties({"iterator"})
public class GlossaryList {

    @JsonUnwrapped
    private Page<Glossary> page;

    public GlossaryList(Page<Glossary> page) {
        this.page = page;
    }

    public Page<Glossary> getPage() {
        return page;
    }
}
