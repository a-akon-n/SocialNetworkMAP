package ro.ubbcluj.map.ui;

import ro.ubbcluj.map.domain.FriendRequest;
import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validators.ValidationException;
import ro.ubbcluj.map.service.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Console {
    private final UserService userService;
    private final FriendshipService friendshipService;
    private final Network networkAdjList;
    private final MessageService messageService;
    private final FriendRequestService friendRequestService;
    Scanner input = new Scanner(System.in);

    public Console(UserService userService, FriendshipService friendshipService, Network network,
                   MessageService messageService, FriendRequestService friendRequestService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.networkAdjList = network;
        this.messageService = messageService;
        this.friendRequestService = friendRequestService;
    }


    //Menu
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
        System.out.println("7. Send message");
        System.out.println("8. Delete message");
        System.out.println("9. Reply to message");
        System.out.println("sm. Show all messages");
        System.out.println("sc. Show conversation");
        System.out.println("suf. Show user's friends");
        System.out.println("10. Send friend request");
        System.out.println("11. Delete friend request");
        System.out.println("sur. Show friend requests of a user");
        System.out.println("sar. Show all friend requests");
        System.out.println("rr. Resolve friend requests");
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
            else if(Objects.equals(command, "7")){
                addMessage();
            }
            else if(Objects.equals(command, "8")){
                deleteMessage();
            }
            else if(Objects.equals(command, "sm")){
                showMessages();
            }
            else if(Objects.equals(command, "9")){
                addReply();
            }
            else if(Objects.equals(command, "sc")){
                showConversation();
            }
            else if(Objects.equals(command, "suf")){
                showUserFriends();
            }
            else if(Objects.equals(command, "10")){
                sendRequest();
            }
            else if(Objects.equals(command, "11")){
                deleteRequest();
            }
            else if(Objects.equals(command, "sur")){
                getRequests();
            }
            else if(Objects.equals(command, "sar")){
                getAllRequests();
            }
            else if(Objects.equals(command, "rr")){
                resolveRequest();
            }
            else{
                System.out.println("Invalid command, try again.");
            }
        }
    }

    private void showUserFriends() {
        try{
            System.out.println("Insert the id of the user to show its friends: ");
            Long id_user = input.nextLong();
            List<Friendship> friendships = new ArrayList<>();
            for(Friendship friendship:friendshipService.findAll()){
                friendships.add(friendship);
//                if (Objects.equals(friendship.getUser1(), id_user)) {
//                    System.out.print(userService.findOne(friendship.getUser2()).getFirstName() + " | ");
//                    System.out.print(userService.findOne(friendship.getUser2()).getLastName() + " | ");
//                    System.out.println(friendship.getDate());
//                } else if (Objects.equals(friendship.getUser2(), id_user)) {
//                    System.out.print(userService.findOne(friendship.getUser1()).getFirstName() + " | ");
//                    System.out.print(userService.findOne(friendship.getUser1()).getLastName() + " | ");
//                    System.out.println(friendship.getDate());
//                }
            }
            friendships
                    .stream()
                    .filter(x->x.getUser1().equals(id_user) || x.getUser2().equals(id_user))
                    .forEach(System.out::println);
        }catch(IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    //Network
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

    //Friendship
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
            Date date = new Date(System.currentTimeMillis());

            Friendship friendship = new Friendship(id_friendship, id_user1, id_user2, date);
            friendshipService.addEntity(friendship);
            networkAdjList.addEdge(friendship);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }

    //User
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

    //Message
    private void addMessage(){
        try{
            Map<Long, Long> ids = new HashMap<>();
            int i = 0;
            Long id_message = null;
            for(Message message:messageService.findAll()){
                ids.put(message.getId(), message.getId());
            }
            id_message = getNextId(ids, i, id_message);

            System.out.println("From (id): ");
            Long fromId = Long.parseLong(input.next());
            User from = userService.findOne(fromId);

            System.out.println("To (id1,id2, ...): ");
            String toString = input.next();
            String[] stringIds = toString.split(",");
            ArrayList<Long> toIds = new ArrayList<>();
            Arrays.stream(stringIds).forEach(id -> toIds.add(Long.parseLong(id)));
            List<User> to = new ArrayList<>();
            toIds.forEach(id -> to.add(userService.findOne(id)));

            System.out.println("Message: ");
            String m = input.nextLine();

            LocalDateTime localDateTime = LocalDateTime.now();

            Message message = new Message(id_message, from, to, m, localDateTime, null);
            messageService.addEntity(message);

        }catch(ValidationException|IllegalArgumentException| InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void deleteMessage(){
        try{
            System.out.print("Insert the id of the message to delete from the database: ");
            Long id_message = input.nextLong();
            userService.deleteEntity(id_message);
        }catch(ValidationException|IllegalArgumentException|InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void showMessages(){
        try{
            messageService.findAll().forEach(id -> System.out.println(id.toStringSimplified()));
        }catch(ValidationException|IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    private void addReply(){
        try{
            Map<Long, Long> ids = new HashMap<>();
            int i = 0;
            Long id_message = null;
            for(Message message:messageService.findAll()){
                ids.put(message.getId(), message.getId());
            }
            id_message = getNextId(ids, i, id_message);

            System.out.println("Reply to message with id: ");
            Long replyId = input.nextLong();
            Message replyTo = messageService.findOne(replyId);

            System.out.println("From (id): ");
            Long fromId = Long.parseLong(input.next());
            User from = userService.findOne(fromId);

            System.out.println("To (id1,id2, ...): ");
            String toString = input.next();
            String[] stringIds = toString.split(",");
            ArrayList<Long> toIds = new ArrayList<>();
            Arrays.stream(stringIds).forEach(id -> toIds.add(Long.parseLong(id)));
            List<User> to = new ArrayList<>();
            toIds.forEach(id -> to.add(userService.findOne(id)));

            System.out.println("Message: ");
            String m = input.nextLine();

            LocalDateTime localDateTime = LocalDateTime.now();

            Message message = new Message(id_message, from, to, m, localDateTime, replyTo);
            messageService.addEntity(message);

        }catch(ValidationException|IllegalArgumentException| InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void showConversation(){
        try{
            System.out.println("Insert id of the first user: ");
            Long id1 = input.nextLong();
            System.out.println("Insert id of the second user: ");
            Long id2 = input.nextLong();

            List<Message> conversation = messageService.getConversation(id1, id2);

            conversation.forEach(m -> System.out.println(m.toStringSimplified()));

        } catch(IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    //Friend requests
    private void sendRequest(){
        try {
            System.out.println("Insert the id of the user sending the request: ");
            Long from = input.nextLong();

            System.out.println("Insert the id of the user recieving the request: ");
            Long to = input.nextLong();

            friendRequestService.addEntity(new FriendRequest(0L, from, to, "pending"));
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void deleteRequest(){
        try {
            System.out.println("Insert id of the request: ");
            friendRequestService.deleteEntity(input.nextLong());
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void getRequests(){
        try{
            System.out.println("Insert the id of a user: ");
            List<FriendRequest> all = friendRequestService.getRequestsOf(input.nextLong());
            all.forEach(System.out::println);
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }

    }
    private void getAllRequests(){
        try {
            friendRequestService.findAll().forEach(System.out::println);
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e){
            e.printStackTrace();
        }
    }
    private void resolveRequest(){
        try {
            System.out.println("Inert the id of the request: ");
            Long id = input.nextLong();

            System.out.println("Accept or reject? (a/r): ");
            String action = input.next();

            FriendRequest fr = friendRequestService.findOne(id);

            if (Objects.equals(action, "a")) {
                friendRequestService.updateRequestStatus(id, "approved");

                Map<Long, Long> ids = new HashMap<>();
                int i = 0;
                Long id_friendship = null;
                for(Friendship friendship: friendshipService.findAll()){
                    ids.put(friendship.getId(), friendship.getId());
                }
                id_friendship = getNextId(ids, i, id_friendship);

                friendshipService.addEntity(
                        new Friendship(
                                id_friendship, fr.getFrom(), fr.getTo(),
                                Date.valueOf(LocalDate.now()
                                )
                        )
                );

            } else if (Objects.equals(action, "r")) {
                friendRequestService.updateRequestStatus(id, "rejected");
            } else {
                System.out.println("Wrong option!");
            }
        } catch(ValidationException | IllegalArgumentException | InputMismatchException e) {
            e.printStackTrace();
        }
    }

}
