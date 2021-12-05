package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.repository.Repository;


public abstract class AbstractService<ID, E extends Entity<ID>> {
    public Repository<ID,E> repository;

    public void addEntity(E entity){
        repository.save(entity);
    }
    public void deleteEntity(ID id){
        repository.delete(id);
    }
    public E findOne(ID id){
        return repository.findOne(id);
    }
    public Iterable<E> findAll(){
        return repository.findAll();
    }
    public Long getNumberOf(){
        return repository.getNumberOf();
    }
}
