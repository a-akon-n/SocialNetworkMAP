package com.example.domain.validators;

import com.example.domain.Friendship;
import com.example.repository.SQLUserRepository;


public class FriendshipValidator implements Validator<Friendship> {
    private final SQLUserRepository repo;

    public FriendshipValidator(SQLUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(repo.findOne(entity.getUser1()) == null){
            throw new ValidationException("User 1 doesn't exist");
        }
        if(repo.findOne(entity.getUser2()) == null){
            throw new ValidationException("User 2 doesn't exist");
        }
    }
}

