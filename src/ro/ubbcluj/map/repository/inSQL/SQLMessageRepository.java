package ro.ubbcluj.map.repository.inSQL;

import ro.ubbcluj.map.domain.Message;
import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SQLMessageRepository implements Repository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Message> validator;

    public SQLMessageRepository(Validator<Message> validator, String url, String username, String password) {
        this.validator = validator;
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Message findOne(Long aLong) {
        Message m = null;
        String sql = "select * from messages where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            //Get message
            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            //Get id, id_user_from, message, dateTime
            long id = resultSet.getLong("id");
            long id_sender = resultSet.getLong("id_user_from");
            String message = resultSet.getString("message");
            LocalDate date = resultSet.getDate("date").toLocalDate();
            LocalTime time = resultSet.getTime("time").toLocalTime();
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            List<User> to = new ArrayList<>();

            //Get all conections to this message
            sql = "select * from messages_users where id_message = ?";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setLong(1, id);
            resultSet = pStatement.executeQuery();


            //Get "to" user list
            while (resultSet.next()){
                String newSql = "select * from users where id_user = ?";
                PreparedStatement userStatement = connection.prepareStatement(newSql);
                userStatement.setLong(1, resultSet.getLong("id_reciever"));
                ResultSet userResult = userStatement.executeQuery();
                userResult.next();

                Long id_user = userResult.getLong("id_user");
                String first_name = userResult.getString("first_name");
                String last_name = userResult.getString("last_name");

                User user = new User(id_user, first_name, last_name);
                if(!to.contains(user)) to.add(user);

            }

            //Get from user
            sql = "select * from users where id_user = ?";
            PreparedStatement prStatement = connection.prepareStatement(sql);
            prStatement.setLong(1, id_sender);
            resultSet = prStatement.executeQuery();
            resultSet.next();

            Long id_user = resultSet.getLong("id_user");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");

            User from = new User(id_user, first_name, last_name);

            m = new Message(id, from, to, message, dateTime);
            return m;

        }catch (SQLException e){
            e.printStackTrace();
        }

        return m;
    }

        @Override
    public Iterable<Message> findAll() {

        Set<Message> messages = new HashSet<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                long id = resultSet.getLong("id");
                messages.add(findOne(id));
            }
            return messages;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public void save(Message entity) {
        String sql = "insert into messages(id, id_user_from, message, date, time) values(?, ?, ?, ?, ?)";
        validator.validate(entity);

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, entity.getId());
            statement.setLong(2, entity.getFrom().getId());
            statement.setString(3, entity.getMessage());
            statement.setDate(4, Date.valueOf(entity.getDate().toLocalDate()));
            statement.setTime(5, Time.valueOf(entity.getDate().toLocalTime()));

            statement.executeUpdate();

            for(User u : entity.getTo()){
                sql = "insert into messages_users(id_message, id_sender, id_reciever) values(?, ?, ?)";
                PreparedStatement pStatement = connection.prepareStatement(sql);
                pStatement.setLong(1, entity.getId());
                pStatement.setLong(2, entity.getFrom().getId());
                pStatement.setLong(3, u.getId());

                pStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long aLong) {
        String sql = "delete from messages where id = ?";

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
            PreparedStatement statement = connection.prepareStatement("select count(id) from messages");
            ResultSet resultSet = statement.executeQuery()) {

            resultSet.next();
            numberOfElem = resultSet.getLong("count");

            return numberOfElem;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberOfElem;
    }
}
