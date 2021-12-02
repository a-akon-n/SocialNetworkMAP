package ro.ubbcluj.map.repository.inSQL;

import ro.ubbcluj.map.domain.User;
import ro.ubbcluj.map.domain.validators.Validator;
import ro.ubbcluj.map.repository.inMemory.InMemoryRepository;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class SQLUserRepository extends InMemoryRepository<Long, User> {
    private String url;
    private String username;
    private String password;

    public SQLUserRepository(Validator<User> validator, String url, String username, String password) {
        super(validator);
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public User findOne(Long aLong) {
        User user = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement("select * from users where id_user="+aLong);
        ResultSet resultSet = statement.executeQuery()){
            resultSet.next();
            Long id = resultSet.getLong("id_user");
            String first_name = resultSet.getString("first_name");
            String last_name = resultSet.getString("last_name");

            user = new User(id, first_name, last_name);
            return user;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from users");
            ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                Long id = resultSet.getLong("id_user");
                String first_name = resultSet.getString("first_name");
                String last_name = resultSet.getString("last_name");

                User user = new User(id, first_name, last_name);
                users.add(user);
            }
            return users;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User save(User entity) {
        String sql = "insert into users(id_user, first_name, last_name) values(?, ?, ?)";
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, Math.toIntExact(entity.getId()));
            statement.setString(2, entity.getFirstName());
            statement.setString(3, entity.getLastName());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long aLong) {
        try(Connection connection = DriverManager.getConnection(url, username,password);
        PreparedStatement statement = connection.prepareStatement("delete from users where id_user = " + aLong)){
            statement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long getNumberOf() {
        Long numberOfElem = null;
        try(Connection connection = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = connection.prepareStatement("select count(id_user) from users");
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