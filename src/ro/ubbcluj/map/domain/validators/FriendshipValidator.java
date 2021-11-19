package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{

    @Override
    public void validate(Friendship friendship) throws ValidationException {
        if(friendship.getUser1() == null)
            throw new ValidationException("Id1 can't be null");
        if(friendship.getUser2() == null)
            throw new ValidationException("Id2 can't be null");
        if(friendship.getUser1().equals(friendship.getUser2()))
            throw new ValidationException("Id1 can't be the same as Id2!");

        //TODO: Check if users exist in repo
    }

}
