package com.example.repository;

import com.example.domain.FriendRequest;
import com.example.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLFriendRequestRepository implements Repository<Long, FriendRequest> {

    private final String url;
    private final String username;
    private final String password;
    private final Validator<FriendRequest> validator;

    public SQLFriendRequestRepository(Validator<FriendRequest> validator, String url, String username, String password){
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public FriendRequest findOne(Long aLong) {
        FriendRequest fr = null;
        String sql = "select * from friend_requests where id = ?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Long id = resultSet.getLong("id");
            Long from = resultSet.getLong("id_from");
            Long to = resultSet.getLong("id_to");
            String status = resultSet.getString("status");

            fr = new FriendRequest(id, from, to, status);
        }catch (SQLException e){
            e.printStackTrace();
        }

        return fr;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        List<FriendRequest> friendRequests = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from friend_requests");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("id");
                Long from = resultSet.getLong("id_from");
                Long to = resultSet.getLong("id_to");
                String status = resultSet.getString("status");

                friendRequests.add(new FriendRequest(id, from, to, status));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return friendRequests;
    }

    @Override
    public void save(FriendRequest entity) {
        String sql = "insert into friend_requests(id_from, id_to, status) values(?, ?, ?)";
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, entity.getFrom());
            statement.setLong(2, entity.getTo());
            statement.setString(3, entity.getStatus());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        String sql = "delete from friend_requests where id = ?";

        try(Connection connection = DriverManager.getConnection(url, username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, aLong);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Long getNumberOf() {
        Long numberOfElem = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(id) from friend_requests");
            ResultSet resultSet = statement.executeQuery()){

            resultSet.next();
            numberOfElem = resultSet.getLong("count");

            return numberOfElem;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfElem;
    }
}
