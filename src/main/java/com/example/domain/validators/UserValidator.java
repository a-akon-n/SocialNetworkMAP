package com.example.domain.validators;

import com.example.domain.User;

public class UserValidator implements Validator<User> {
    @Override
    public void validate(User entity) throws ValidationException {
        if(entity.getFirstName() == null){
            throw new ValidationException("First name of user " + entity.getId() + "is null");
        }
        if(entity.getLastName() == null){
            throw new ValidationException("Last name of user " + entity.getId() + " is null!");
        }
        if(entity.getFriends().contains(entity)){
            throw new ValidationException("An user can't be in his own friend list");
        }
        if(entity.getFirstName().equals("")){
            throw new ValidationException("First name can't be null");
        }
        if(entity.getLastName().equals("")){
            throw new ValidationException("Last name can't be null");
        }
    }
}
