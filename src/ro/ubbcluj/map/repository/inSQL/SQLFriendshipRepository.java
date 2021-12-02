package ro.ubbcluj.map.repository.inSQL;

import ro.ubbcluj.map.domain.Friendship;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.inMemory.InMemoryRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SQLFriendshipRepository extends InMemoryRepository<Long, Friendship> {
    private String url;
    private String username;
    private String password;

    public SQLFriendshipRepository(Validator<Friendship> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Friendship findOne(Long aLong) {
        Friendship friendship = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships where id_friendship = " +  aLong);
            ResultSet resultSet = statement.executeQuery()){
            resultSet.next();

            Long id = resultSet.getLong("id_friendship");
            Long id_user1 = resultSet.getLong("id_user1");
            Long id_user2 = resultSet.getLong("id_user2");

            friendship = new Friendship(id, id_user1, id_user2);
            return friendship;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return friendship;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friendships");
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                Long id = resultSet.getLong("id_friendship");
                Long id_user1 = resultSet.getLong("id_user1");
                Long id_user2 = resultSet.getLong("id_user2");

                Friendship friendship = new Friendship(id, id_user1, id_user2);

                friendships.add(friendship);
            }
            return friendships;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        String sql = "insert into friendships(id_friendship, id_user1, id_user2) values(?, ?, ?)";
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, entity.getId().intValue());
            statement.setInt(2, entity.getUser1().intValue());
            statement.setInt(3, entity.getUser2().intValue());

            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Long aLong) {

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("delete from friendships where id_friendship = " + aLong)){
            statement.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getNumberOf() {
        Long numberOfElem = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(id_friendship) from friendships");
            ResultSet resultSet = statement.executeQuery()){
            resultSet.next();

            numberOfElem = resultSet.getLong("count");
            return numberOfElem;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return numberOfElem;
    }
}