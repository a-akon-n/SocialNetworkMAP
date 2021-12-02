package ro.ubbcluj.map.domain.validators;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.inMemory.InMemoryRepository;
import ro.ubbcluj.map.repository.inSQL.SQLUserRepository;


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
