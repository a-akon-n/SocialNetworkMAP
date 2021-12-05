package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.validators.FriendshipValidator;
import ro.ubbcluj.map.domain.validators.UserValidator;
import ro.ubbcluj.map.repository.inSQL.SQLFriendshipRepository;
import ro.ubbcluj.map.repository.inSQL.SQLUserRepository;
import ro.ubbcluj.map.service.FriendshipService;
import ro.ubbcluj.map.service.Network;
import ro.ubbcluj.map.service.UserService;
import ro.ubbcluj.map.ui.Console;

public class Main {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/social_network";
        String username = "postgres";
        String password = "postgre";

        UserValidator userValidator = new UserValidator();
        SQLUserRepository userRepository = new SQLUserRepository(userValidator, url, username, password);
        UserService userService = new UserService(userRepository);

        FriendshipValidator friendshipValidator = new FriendshipValidator(userRepository);
        SQLFriendshipRepository friendshipRepository = new SQLFriendshipRepository(friendshipValidator, url, username, password);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);

        Network network = new Network(userRepository, friendshipRepository);

        Console userInterface = new Console(userService, friendshipService, network);

        userInterface.run_console();
    }
}
