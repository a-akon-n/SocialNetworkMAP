package ro.ubbcluj.map.repository;

import ro.ubbcluj.map.domain.Entity;

import java.util.ArrayList;

public interface Repository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    Long getNumberOf();

    void save(E entity);
    void delete(ID id);
}
