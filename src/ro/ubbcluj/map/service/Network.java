package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.repository.FileRepository;

import java.util.*;

public class Network implements Graph{

    private Map<User, List<User>> adjList;
    FileRepository<Long, User> users;
    FileRepository<Long, Friendship> friendships;

    public Network(FileRepository<Long, User> users, FileRepository<Long, Friendship> friendships) {
        this.users = users;
        this.friendships = friendships;
        this.adjList = new HashMap<>();
        createGraph();
    }

    public Map<User, List<User>> getAdjList() {
        return adjList;
    }

    public void setAdjList(Map<User, List<User>> adjList) {
        this.adjList = adjList;
    }

    @Override
    public void addVertex(User user) {
        adjList.putIfAbsent(user, new ArrayList<>());
    }

    @Override
    public void removeVertex(User user) {
        adjList.values().stream().forEach(e -> e.remove(user));
        adjList.remove(user);
    }

    @Override
    public void addEdge(Friendship friendship) {
        User user1 = friendship.getUser1();
        User user2 = friendship.getUser2();
        adjList.get(user1).add(user2);
        adjList.get(user2).add(user1);
    }

    @Override
    public void removeEdge(Friendship friendship) {
        User user1 = friendship.getUser1();
        User user2 = friendship.getUser2();
        List<User> friend1 = adjList.get(user1);
        List<User> friend2 = adjList.get(user2);
        if(friend1 != null)
            friend1.remove(user2);
        if(friend2 != null)
            friend2.remove(user1);
    }

    @Override
    public void createGraph() {
        for(User user:users.findAll()){
            addVertex(user);
        }
        for(Friendship friendship:friendships.findAll()){
            addEdge(friendship);
        }
    }

    @Override
    public Set<User> DFS(User root) {
        Set<User> visited = new LinkedHashSet<>();
        Stack<User> stack = new Stack<User>();
        stack.push(root);
        while (!stack.isEmpty()){
            User user = stack.pop();
            if(!visited.contains(user)){
                visited.add(user);
                for(User u:getAdjList().get(user)){
                    stack.push(u);
                }
            }
        }
        return visited;
    }

    @Override
    public void printGraph() {
        for(User user:users.findAll()){
            System.out.println(user + "  \\||^_^||/  ");
            for(User u:getAdjList().get(user)){
                System.out.println("\t" + u + "\t----\t");
            }
        }
    }

    @Override
    public Set<Set<User>> numberOfConnectedComponents() {
        Set<Set<User>> components = new HashSet<>();
        ArrayList<Boolean> visited = new ArrayList<>();
        for (User user: users.findAll()) {
            visited.add(false);
        }
        for (User user:users.findAll()) {
            if (!visited.get(Math.toIntExact(user.getId() - 1))) {
                Set<User> userSet = DFS(user);
                for(User u:userSet){
                    visited.set(Math.toIntExact(u.getId() - 1), true);
                }
                components.add(userSet);
            }
        }
        return components;
    }

    @Override
    public int no_ofComponents() {
        Set<Set<User>> components = numberOfConnectedComponents();
        return components.size();
    }

    @Override
    public Set<User> longestConnectedComponent() {
        int max = 0;
        Set<User> setMax = new HashSet<>();
        Set<Set<User>> userSet = numberOfConnectedComponents();
        for(Set set:userSet){
            if(set.size() > max){
                max = set.size();
                setMax = set;
            }
        }
        return setMax;
    }
}
