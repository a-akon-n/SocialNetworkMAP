package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.FileRepository;

import java.util.ArrayList;

public class FriendshipService extends AbstractService<Long, Friendship> {

    private FileRepository<Long, User> userRepo;
    public FriendshipService(FileRepository<Long, Friendship> repository, FileRepository<Long, User> userRepo) {
        super(repository);
        this.userRepo = userRepo;
    }

    @Override
    public void addEntity(Friendship entity) {
        super.addEntity(entity);
        entity.getUser1().addFriend(entity.getUser2());
        entity.getUser2().addFriend(entity.getUser1());
    }

    @Override
    public void deleteEntity(Long aLong) {
        Friendship friendship = findOne(aLong);
        friendship.getUser1().deleteFriend(friendship.getUser2());
        friendship.getUser2().deleteFriend(friendship.getUser1());
        super.deleteEntity(aLong);
    }

    @Override
    public void writeData(ArrayList<Friendship> entities) {
        StringBuilder data = new StringBuilder("");
        for(Friendship friendship:entities){
            data.append(friendship.getId()).append(";").append(friendship.getUser1().getId()).append(";").append(friendship.getUser2().getId()).append("\n");
        }
        repository.writeData(data.toString());
    }

    @Override
    public void loadData() {
        ArrayList<String> data = repository.loadData();
        for(int i = 0; i < data.size(); i+=3){
            Long id_friendship = Long.valueOf(data.get(i));
            User user1 = userRepo.findOne(Long.valueOf(data.get(i+1)));
            User user2 = userRepo.findOne(Long.valueOf(data.get(i+2)));
            Friendship friendship = new Friendship(id_friendship, user1, user2);
            repository.save(friendship);
        }
    }
}
