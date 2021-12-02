package ro.ubbcluj.map.repository.inMemory;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryRepository<ID, E extends Entity<ID>> implements Repository<ID, E> {
    private final Map<ID, E> entities;
    protected final Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.validator = validator;
        entities = new HashMap<>();
    }

    @Override
    public E findOne(ID id) {
        if(id==null){
            throw new IllegalArgumentException("ID must not be null");
        }
        if(entities.get(id) == null){
            throw new ValidationException("Entity is doesn't exist");
        }
        return entities.get(id);
    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) {
        if(entity==null)
            throw new IllegalArgumentException("Entity must not be null");
        validator.validate(entity);
        if(entities.get(entity.getId())!=null)
            return entity;
        entities.put(entity.getId(),entity);
        return null;
    }

    @Override
    public E delete(ID id) {
        if(this.findOne(id)==null)
            throw new IllegalArgumentException("Entity with the id " + id + " is null");
        entities.remove(id);
        return null;
    }

    @Override
    public Long getNumberOf() {
        return (long) entities.size();
    }

}
