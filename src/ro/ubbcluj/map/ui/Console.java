package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.service.FriendshipService;
import ro.ubbcluj.map.service.Network;
import ro.ubbcluj.map.service.UserService;

import java.util.*;

public class Console {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Network networkAdjList;
    Scanner input = new Scanner(System.in);

    public Console(UserService userService, FriendshipService friendshipService, Network network) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkAdjList = network;
    }


    public static void menu(){
        System.out.println("-----------------------------MENU----------------------------------");
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("su. Show all users");
        System.out.println("3. Add friendship");
        System.out.println("4. Delete friendship");
        System.out.println("sf. Show all friendships");
        System.out.println("5. Number of communities");
        System.out.println("6. Most sociable community");
        System.out.println("sn. Show network");
        System.out.println("x. Stop");
        System.out.println("-------------------------------------------------------------------");
    }

    public void run_console(){
        while(true){
            menu();
            System.out.println("Insert command: ");
            String command = input.next();
            if(Objects.equals(command, "x")) break;
            else if(Objects.equals(command, "1")){
                addUser();
            }
            else if(Objects.equals(command, "2")){
                deleteUser();
            }
            else if(Objects.equals(command, "su")){
                showUsers();
            }
            else if(Objects.equals(command, "3")){
                addFriendship();
            }
            else if(Objects.equals(command, "4")){
                deleteFriendship();
            }
            else if(Objects.equals(command, "sf")){
                showFriendships();
            }
            else if(Objects.equals(command, "5")){
                numberOfCommunities();
            }
            else if(Objects.equals(command, "6")){
                mostSociableCommunity();
            }
            else if(Objects.equals(command, "sn")){
                showNetwork();
            }
            else{
                System.out.println("Invalid command, try again.");
            }
        }
    }

    private void showNetwork() {
        Map<Long, List<Long>> network = networkAdjList.getAdjList();
        for(Long i = 1L; i <= userService.getNumberOf(); i++){
            System.out.println(userService.findOne(i) + "  \\||^_^||/  ");
            for(Long user:network.get(i)){
                System.out.println("\t" + userService.findOne(user) + "\t----\t");
            }
        }
    }

    private void mostSociableCommunity() {
        System.out.println("The most sociable community is: " + networkAdjList.longestConnectedComponent());
    }

    private void numberOfCommunities() {
        System.out.println("The Number of communities in the network is: " + networkAdjList.no_ofComponents());
    }

    private void showFriendships() {
        try{
            for(Friendship friendship:friendshipService.findAll()){
                System.out.println(friendship);
            }
        }catch(ValidationException|IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void deleteFriendship() {
        try{
            System.out.println("Insert the id of the friendship to delete from the database");
            Long id_friendship = input.nextLong();
            networkAdjList.removeEdge(friendshipService.findOne(id_friendship));
            friendshipService.deleteEntity(id_friendship);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    private void addFriendship() {
        try{
            Map<Long, Long> ids = new HashMap<>();
            int i = 0;
            Long id_friendship = null;
            for(Friendship friendship: friendshipService.findAll()){
                ids.put(friendship.getId(), friendship.getId());
            }
            id_friendship = getNextId(ids, i, id_friendship);

            System.out.println("Insert the id of the first user");
            Long id_user1 = input.nextLong();
            System.out.println("Insert the id of the second user");
            Long id_user2 = input.nextLong();

            Friendship friendship = new Friendship(id_friendship, id_user1, id_user2);
            friendshipService.addEntity(friendship);
            networkAdjList.addEdge(friendship);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    private void showUsers() {
        try{
            for(User user:userService.findAll()){
                System.out.println(user);
            }
        }catch(ValidationException|IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    private void deleteUser() {
        try{
            System.out.print("Insert the id of the user to delete from the database: ");
            Long id_user = input.nextLong();
            for(Friendship friendship:friendshipService.findAll()){
                if(Objects.equals(friendship.getUser1(), id_user) || Objects.equals(friendship.getUser2(), id_user))
                    friendshipService.deleteEntity(friendship.getId());
            }
            networkAdjList.removeVertex(userService.findOne(id_user));
            userService.deleteEntity(id_user);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    private void addUser() {
        try{
            Map<Long, Long> ids = new HashMap<>();
            int i = 0;
            Long id_user = null;
            for(User user:userService.findAll()){
                ids.put(user.getId(), user.getId());
            }
            id_user = getNextId(ids, i, id_user);
            System.out.println("Insert the first name of the user.");
            String first_name = input.next();
            System.out.println("Insert the last name of the user.");
            String last_name = input.next();
            User user = new User(id_user, first_name, last_name);
            userService.addEntity(user);
            networkAdjList.addVertex(user);
        }catch(ValidationException|IllegalArgumentException| InputMismatchException e){
            e.printStackTrace();
        }
    }

    private Long getNextId(Map<Long, Long> ids, int i, Long id_user) {
        int size = ids.size();
        while(size != i){
            Map.Entry<Long, Long> entry = ids.entrySet().iterator().next();
            if(ids.get(entry.getKey()) != i + 1){
                id_user = (long) i + 1;
                break;
            }
            ids.remove(entry.getKey());
            i++;
        }
        if(id_user == null)
            id_user = (long) (size + 1);
        return id_user;
    }

}
