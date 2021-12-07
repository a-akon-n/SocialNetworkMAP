package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.repository.Repository;

import java.util.Objects;

public class FriendRequestService extends AbstractService<Long, FriendRequest>{
    public FriendRequestService(Repository<Long, FriendRequest> friendRequestRepository) {
        repository = friendRequestRepository;
    }
}
