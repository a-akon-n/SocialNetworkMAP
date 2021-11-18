package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.FileRepository;

import java.util.ArrayList;

public class UserService extends AbstractService<Long, User> {

    public UserService(FileRepository<Long, User> repository) {
        super(repository);
    }

    @Override
    public void writeData(ArrayList<User> entities) {
        StringBuilder data = new StringBuilder();
        for(User user:entities){
            data.append(user.getId()).append(";").append(user.getFirstName()).append(";").append(user.getLastName()).append("\n");
        }
        repository.writeData(data.toString());
    }

    @Override
    public void loadData() {
        ArrayList<String> data = repository.loadData();
        for(int i = 0; i < data.size(); i+=3){
            Long id_user = Long.valueOf(data.get(i));
            String first_name = data.get(i+1);
            String last_name = data.get(i+2);
            User user = new User(id_user, first_name, last_name);
            repository.save(user);
        }
    }

}
