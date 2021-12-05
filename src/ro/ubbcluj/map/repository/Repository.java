package ro.ubbcluj.map.repository;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.ValidationException;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    void save(E entity);
    void delete(ID id);
    Long getNumberOf();
}


