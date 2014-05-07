package com.aimprosoft.glossary.common.service;

import com.aimprosoft.glossary.common.model.BusinessModel;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BaseCrudService<T extends BusinessModel> {

    Page<T> getCurrentPage(int startRow, int pageSize);

    boolean exists(Long id);

    long count();

    T getById(Long id);

    @Transactional(readOnly = false)
    void add(T entity);

    @Transactional(readOnly = false)
    void update(T entity);

    @Transactional(readOnly = false)
    void remove(T entity);

    @Transactional(readOnly = false)
    void removeById(Long id);

}
