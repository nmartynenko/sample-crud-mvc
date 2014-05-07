package com.aimprosoft.glossary.common.service.impl;

import com.aimprosoft.glossary.common.model.BusinessModel;
import com.aimprosoft.glossary.common.service.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class BaseCrudServiceImpl<T extends BusinessModel, P extends JpaRepository<T, Long>> implements BaseCrudService<T> {

    protected P persistence;

    @Autowired
    public void setPersistence(P persistence){
        this.persistence = persistence;
    }

    @Override
    public Page<T> getCurrentPage(int startRow, int pageSize) {
        Pageable pageable = null;

        if (startRow >= 0 && pageSize > 0) {
            pageable = new PageRequest(startRow / pageSize, pageSize);
        }

        return persistence.findAll(pageable);
    }

    @Override
    public boolean exists(Long id) {
        return persistence.exists(id);
    }

    @Override
    public long count() {
        return persistence.count();
    }

    @Override
    public T getById(Long id) {
        return persistence.findOne(id);
    }

    @Override
    public void add(T entity) {
        update(entity);
    }

    @Override
    public void update(T entity) {
        persistence.save(entity);
    }

    @Override
    public void remove(T entity) {
        persistence.delete(entity);
    }

    @Override
    public void removeById(Long id) {
        persistence.delete(id);
    }
}
