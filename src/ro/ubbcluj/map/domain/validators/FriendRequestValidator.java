package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.FriendRequest;

import java.util.Objects;

public class FriendRequestValidator implements Validator<FriendRequest>{
    @Override
    public void validate(FriendRequest entity) throws ValidationException {
        if(!Objects.equals(entity.getStatus(), "approved") &&
                !Objects.equals(entity.getStatus(), "pending") &&
                !Objects.equals(entity.getStatus(), "rejected")){
            throw new ValidationException("Friend request status has to be one of" +
                    " ['approved', 'pending', 'rejected']");
        }
    }
}
