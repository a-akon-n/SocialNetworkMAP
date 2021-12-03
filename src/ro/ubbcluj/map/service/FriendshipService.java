package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.repository.Repository;

import java.util.Objects;

public class FriendshipService extends AbstractService<Long, Friendship> {
    public FriendshipService(Repository<Long, Friendship> friendshipRepository) {
        repository = friendshipRepository;
    }

    @Override
    public void addEntity(Friendship entity) {
        for(Friendship friendship:repository.findAll()){
            if(Objects.equals(friendship.getUser1(), entity.getUser1()) && Objects.equals(friendship.getUser2(), entity.getUser2())){
                throw new IllegalArgumentException("Entitatea deja exista!");
            }
        }
        super.addEntity(entity);
    }
}
