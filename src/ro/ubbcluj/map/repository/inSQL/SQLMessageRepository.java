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

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            long id = resultSet.getLong("id");
            long id_user_from = resultSet.getLong("id_user_from");
            String message = resultSet.getString("last_name");
            LocalDate date = resultSet.getDate("date").toLocalDate();
            LocalTime time = resultSet.getTime("time").toLocalTime();
            LocalDateTime dateTime = LocalDateTime.of(date, time);
            List<User> to = new ArrayList<>();

            sql = "select * from from_to where id_user_from = ?";
            PreparedStatement pStatement = connection.prepareStatement(sql);

            pStatement.setLong(1, id_user_from);
            resultSet = pStatement.executeQuery();

            while (resultSet.next()){
                Long id_user = resultSet.getLong("id_user");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");

                User user = new User(id_user, first_name, last_name);
                to.add(user);
            }

            sql = "select * from users where id_user = ?";
            PreparedStatement prStatement = connection.prepareStatement(sql);
            prStatement.setLong(1, id_user_from);
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
                /*long id_user_from = resultSet.getLong("id_user_from");
                String message = resultSet.getString("last_name");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                LocalTime time = resultSet.getTime("time").toLocalTime();
                LocalDateTime dateTime = LocalDateTime.of(date, time);
                List<User> to = new ArrayList<>();

                String sql = "select * from from_to where id_user_from = ?";
                PreparedStatement pStatement = connection.prepareStatement(sql);

                pStatement.setLong(1, id_user_from);
                ResultSet userResultSet = pStatement.executeQuery();

                while (userResultSet.next()){
                    Long id_user = resultSet.getLong("id_user");
                    String first_name = resultSet.getString("first_name");
                    String last_name = resultSet.getString("last_name");

                    User user = new User(id_user, first_name, last_name);
                    to.add(user);
                }

                sql = "select * from users where id_user = ?";
                PreparedStatement prStatement = connection.prepareStatement(sql);
                prStatement.setLong(1, id_user_from);
                userResultSet = prStatement.executeQuery();

                userResultSet.next();
                Long id_user = resultSet.getLong("id_user");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");

                User from = new User(id_user, first_name, last_name);

                messages.add(new Message(id, from, to, message, dateTime));*/

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

            //entity.getTo().stream().forEach();
            for(User u : entity.getTo()){
                sql = "insert into from_to(id_user_from, id_user_to) values(?, ?)";
                PreparedStatement pStatement = connection.prepareStatement(sql);
                pStatement.setLong(1, entity.getFrom().getId());
                pStatement.setLong(2, u.getId());
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
