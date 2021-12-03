package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.Repository;

import java.util.Objects;

public class UserService extends AbstractService<Long, User>{
    public UserService(Repository<Long, User> userRepository) {
        repository = userRepository;
    }

    @Override
    public void addEntity(User entity) {
        for(User user:repository.findAll()){
            if(Objects.equals(user.getFirstName(), entity.getFirstName()) && Objects.equals(user.getLastName(), entity.getLastName())){
                throw new IllegalArgumentException("Entitatea deja exista!");
            }
        }
        super.addEntity(entity);
    }
}
