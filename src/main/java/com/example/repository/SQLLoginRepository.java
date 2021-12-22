package com.example.repository;

import com.example.domain.Login;
import com.example.domain.validators.Validator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLLoginRepository implements Repository<Long, Login> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<Login> validator;

    public SQLLoginRepository(String url, String username, String password, Validator<Login> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Login findOne(Long id) {

        String sql = "select * from passwords where id_user = ?";
        String sizeSql = "select count(*) as nr from passwords where id_user=?";

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            List<Login> all = (ArrayList<Login>)findAll();
            if (all.isEmpty()) return null;

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            Long idUser = resultSet.getLong("id_user");
            String password = resultSet.getString("password");

            return new Login(idUser, password);

        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Login> findAll() {
        List<Login> logins = new ArrayList<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from passwords");
            ResultSet resultSet = statement.executeQuery()){

            while(resultSet.next()){
                Long id = resultSet.getLong("id_user");
                String password = resultSet.getString("password");

                Login login = new Login(id, password);
                logins.add(login);
            }

            return logins;

        }catch(SQLException e){
            e.printStackTrace();
        }
        return logins;
    }

    @Override
    public void save(Login entity) {
        String sql = "insert into passwords(id_user, password) values(?, ?)";
        validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setInt(1, Math.toIntExact(entity.getId()));
            statement.setString(2, String.valueOf(entity.hashCode()));

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "delete from passwords where id_user = ?";

        try(Connection connection = DriverManager.getConnection(url, username,password);
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, id);
            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Long getNumberOf() {
        Long numberOfElem = null;

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select count(id_user) from passwords");
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
