package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.repository.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FriendRequestService extends AbstractService<Long, FriendRequest>{
    public FriendRequestService(Repository<Long, FriendRequest> friendRequestRepository) {
        repository = friendRequestRepository;
    }

    public List<FriendRequest> getRequestsOf(Long id){
        return ((List<FriendRequest>) findAll())
                .stream()
                .filter(fr -> Objects.equals(fr.getTo(), id) || Objects.equals(fr.getFrom(), id))
                .collect(Collectors.toList());
    }

    public void updateRequestStatus(Long id, String status){
        FriendRequest fr = findOne(id);
        deleteEntity(id);
        fr.setStatus(status);
        addEntity(fr);
    }
}
