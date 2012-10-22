package com.aimprosoft.glossary.common.model;

import javax.persistence.*;

@MappedSuperclass
public abstract class BusinessModel implements HibernateModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
