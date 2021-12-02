package ro.ubbcluj.map.service;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.User;

import java.util.Set;

public interface Graph {
    void addVertex(User user);
    void removeVertex(User user);
    void addEdge(Friendship friendship);
    void removeEdge(Friendship friendship);
    void createGraph();

    Set<Long> DFS(User root);

    Set<Set<Long>> numberOfConnectedComponents();
    int no_ofComponents();
    Set<Long> longestConnectedComponent();
}
