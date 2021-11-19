package ro.ubbcluj.map.repository;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.Validator;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID,E> {

    protected final Validator<E> validator;
    Map<ID,E> entities;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id){
        if (id==null)
            throw new IllegalArgumentException("id must be not null");
        if(entities.get(id) == null) throw new RepositoryException("There is no entity with such ID!");
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public void save(E entity) {
        if (entity==null)
            throw new IllegalArgumentException("entity must be not null");
        validator.validate(entity);
        entities.putIfAbsent(entity.getId(), entity);
    }

    @Override
    public void delete(ID id) {
        if(id == null)
            throw new IllegalArgumentException("ID must not be null!");
        if(!entities.containsKey(id)) {
            throw new IllegalArgumentException("There is no entity with such ID!");
        }
        entities.remove(id);
    }

    @Override
    public Long getNumberOf() {
        return (long) entities.size();
    }
}
