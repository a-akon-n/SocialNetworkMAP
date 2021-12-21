package com.example.repository;

import com.example.domain.Entity;
import com.example.domain.validators.ValidationException;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    void save(E entity);
    void delete(ID id);
    Long getNumberOf();
}


