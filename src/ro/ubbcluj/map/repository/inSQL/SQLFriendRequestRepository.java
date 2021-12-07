package ro.ubbcluj.map.repository.inSQL;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.repository.Repository;

public class SQLFriendRequestRepository implements Repository<Long, FriendRequest> {
    @Override
    public FriendRequest findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        return null;
    }

    @Override
    public void save(FriendRequest entity) {

    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public Long getNumberOf() {
        return null;
    }
}
