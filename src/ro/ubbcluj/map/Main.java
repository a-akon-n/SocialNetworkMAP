package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validators.FriendshipValidator;
import ro.ubbcluj.map.domain.validators.UserValidator;
import ro.ubbcluj.map.repository.FileRepository;
import ro.ubbcluj.map.service.FriendshipService;
import ro.ubbcluj.map.service.Network;
import ro.ubbcluj.map.service.UserService;
import ro.ubbcluj.map.ui.Console;

public class Main {

    public static void main(String[] args) {
        UserValidator userValidator = new UserValidator();
        FileRepository<Long, User> userRepository = new FileRepository<Long, User>(userValidator, "data/user.csv");
        UserService userService = new UserService(userRepository);

        FriendshipValidator friendshipValidator = new FriendshipValidator();
        FileRepository<Long, Friendship> friendshipRepository = new FileRepository<Long, Friendship>(friendshipValidator, "data/friendship.csv");
        FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository);

        Network network = new Network(userRepository, friendshipRepository);

        Console userInterface = new Console(userService, friendshipService, network);
        userInterface.run_console();
    }
}
