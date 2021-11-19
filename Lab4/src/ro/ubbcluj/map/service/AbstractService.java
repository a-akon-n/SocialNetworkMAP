package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Entity;
import ro.ubbcluj.map.repository.FileRepository;
import ro.ubbcluj.map.repository.InMemoryRepository;

import java.util.ArrayList;

public abstract class AbstractService<ID, E extends Entity<ID>> {
    FileRepository<ID,E> repository;

    public AbstractService(FileRepository<ID,E> repository) {
        this.repository = repository;
    }
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
    public abstract void writeData(ArrayList<E> entities);
    public abstract void loadData();
}
