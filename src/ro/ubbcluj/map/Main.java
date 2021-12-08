package ro.ubbcluj.map;

import ro.ubbcluj.map.domain.validators.FriendRequestValidator;
import ro.ubbcluj.map.domain.validators.FriendshipValidator;
import ro.ubbcluj.map.domain.validators.MessageValidator;
import ro.ubbcluj.map.domain.validators.UserValidator;
import ro.ubbcluj.map.repository.inSQL.SQLFriendRequestRepository;
import ro.ubbcluj.map.repository.inSQL.SQLFriendshipRepository;
import ro.ubbcluj.map.repository.inSQL.SQLMessageRepository;
import ro.ubbcluj.map.repository.inSQL.SQLUserRepository;
import ro.ubbcluj.map.service.*;
import ro.ubbcluj.map.ui.Console;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String url = "jdbc:postgresql://localhost:5432/social_network";
        String username = "postgres";
        String password = input.next();
        input.nextLine();

        UserValidator userValidator = new UserValidator();
        SQLUserRepository userRepository = new SQLUserRepository(userValidator, url, username, password);
        UserService userService = new UserService(userRepository);

        FriendshipValidator friendshipValidator = new FriendshipValidator(userRepository);
        SQLFriendshipRepository friendshipRepository = new SQLFriendshipRepository(friendshipValidator, url, username, password);
        FriendshipService friendshipService = new FriendshipService(friendshipRepository);

        Network network = new Network(userRepository, friendshipRepository);

        MessageValidator messageValidator = new MessageValidator();
        SQLMessageRepository messageRepository = new SQLMessageRepository(messageValidator, url, username, password);
        MessageService messageService = new MessageService(messageRepository);

        FriendRequestValidator friendRequestValidator = new FriendRequestValidator();
        SQLFriendRequestRepository friendRequestRepository = new SQLFriendRequestRepository(friendRequestValidator, url, username, password);
        FriendRequestService friendRequestService = new FriendRequestService(friendRequestRepository);

        Console userInterface = new Console(userService, friendshipService, network, messageService, friendRequestService);

        userInterface.run_console();
    }
}
