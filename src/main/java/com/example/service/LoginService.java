package com.example.service;

import com.example.domain.Login;
import com.example.repository.Repository;

import java.util.Objects;

public class LoginService extends AbstractService<Long, Login>{

    public LoginService(Repository<Long, Login> repo) { repository = repo; }
    private Long currentUserId;

    public void setCurrentUserId(Long id) { currentUserId = id;}
    public Long getCurrentUserId() {return currentUserId;}

    @Override
    public void addEntity(Login entity) {
        for(Login login:repository.findAll()){
            if(Objects.equals(login.getPassword(), entity.getPassword())){
                throw new IllegalArgumentException("Entitatea deja exista!");
            }
        }
        super.addEntity(entity);
    }

    public boolean passwordIsValid(Login entity){
        return Objects.equals(findOne(entity.getId()).getPassword(), String.valueOf(entity.hashCode()));
    }
}

