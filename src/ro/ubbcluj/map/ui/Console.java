package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.service.FriendshipService;
import ro.ubbcluj.map.service.Network;
import ro.ubbcluj.map.service.UserService;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Console {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Network network;
    Scanner input = new Scanner(System.in);

    public Console(UserService userService, FriendshipService friendshipService, Network network) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.network = network;
    }

    public static void menu(){
        System.out.println("-----------------------------MENU----------------------------------");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. Add friendship");
        System.out.println("4. Delete friendship");
        System.out.println("5. Number of communities");
        System.out.println("6. Most sociable community");
        System.out.println("su. Show user list");
        System.out.println("sf. Show Friendship list");
        System.out.println("sn. Show Network");
        System.out.println("l. Load Data");
        System.out.println("w. Write Data");
        System.out.println("x. Stop");
        System.out.println("-------------------------------------------------------------------");
    }

    public void run_console(){
        while(true){
            menu();
            System.out.println("Insert command: ");
            String command = input.next();
            if(Objects.equals(command, "x")) break;
            else if (Objects.equals(command, "1")){
                addUser();
            }
            else if(Objects.equals(command, "2")){
                deleteUser();
            }
            else if(Objects.equals(command, "3")){
                addFriendship();
            }
            else if(Objects.equals(command, "4")){
                deleteFriendship();
            }
            else if(Objects.equals(command, "5")){
                numberOfCommunities();
            }
            else if(Objects.equals(command, "6")){
                mostSociableCommunity();
            }
            else if(Objects.equals(command, "su")){
                showUsers();
            }
            else if(Objects.equals(command, "sf")){
                showFriendships();
            }
            else if(Objects.equals(command, "sn")){
                showNetwork();
            }
            else if(Objects.equals(command, "l")){
                loadData();
            }
            else if(Objects.equals(command, "w")){
                writeData();
            }
            else{
                System.out.println("Invalid command, try again.");
            }
        }
    }

    private void mostSociableCommunity() {
        System.out.println("The most sociable community is: " + network.longestConnectedComponent());
    }

    private void numberOfCommunities() {
        System.out.println("The number of connected components is: " + network.no_ofComponents());
    }

    private void writeData() {
        ArrayList<User> users = new ArrayList<>();
        for(User user:userService.findAll()){
            users.add(user);
        }
        userService.writeData(users);

        ArrayList<Friendship> friendships = new ArrayList<>();
        for(Friendship friendship: friendshipService.findAll()){
            friendships.add(friendship);
        }
        friendshipService.writeData(friendships);
    }

    private void loadData() {
        userService.loadData();
        friendshipService.loadData();
        network.createGraph();
    }

    private void showNetwork() {
        network.printGraph();
    }

    private void showFriendships() {
        for(Friendship friendship:friendshipService.findAll()){
            System.out.println(friendship);
        }
    }

    private void showUsers() {
        for (User user:userService.findAll()){
            System.out.println(user);
        }
    }

    private void deleteFriendship() {
        System.out.println("Insert the id of the friendship to delete: ");
        Long id = input.nextLong();
        network.removeEdge(friendshipService.findOne(id));
        friendshipService.deleteEntity(id);
    }

    private void addFriendship() {
        try{
            Long id = (Long) friendshipService.getNumberOf() + 1;
            System.out.println("Insert the id of the first user: ");
            long id_user = input.nextLong();
            User user1 = userService.findOne(id_user);
            System.out.println("Insert the id of the second user: ");
            id_user = input.nextLong();
            User user2 = userService.findOne(id_user);
            Friendship friendship = new Friendship(id,user1,user2);
            friendshipService.addEntity(friendship);
            //network.addEdge(friendship);
        }catch(ValidationException|IllegalArgumentException e){
            System.out.println("Error: " + e);
        }
    }

    private void deleteUser() {
        try{
            System.out.println("Insert the id of the user to delete from the repo: ");
            Long id = input.nextLong();
            User user = userService.findOne(id);
            for (Friendship friendship : friendshipService.findAll()) {
                if (friendship.getUser1() == user || friendship.getUser2() == user) {
                    network.removeEdge(friendship);
                    friendshipService.deleteEntity(friendship.getId());
                }
            }
            network.removeVertex(user);
            userService.deleteEntity(id);
        }catch (ValidationException|IllegalArgumentException e){
            System.out.println("Error: " + e);
        }
    }

    private void addUser() {
        try{
            Long id = (Long) userService.getNumberOf() + 1;
            System.out.println("Insert the first name of the user: ");
            String firstName = input.next();
            System.out.println("Insert the last name of the user: ");
            String lastName = input.next();
            User user = new User(id, firstName, lastName);
            userService.addEntity(user);
            network.addVertex(user);
        }catch(ValidationException | IllegalArgumentException e){
            System.out.println("Error: " + e);
        }
    }
}
