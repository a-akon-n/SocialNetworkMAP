package com.example.filter_sem9;

import com.example.controller.LoginController;
import com.example.domain.validators.LoginValidator;
import com.example.repository.SQLLoginRepository;
import com.example.service.LoginService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        String url = "jdbc:postgresql://localhost:5432/social_network";
        String username = "postgres";
        String password = "12345";

        LoginValidator loginValidator = new LoginValidator();
        SQLLoginRepository loginRepository = new SQLLoginRepository(url, username, password, loginValidator);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("loginView.fxml"));

        AnchorPane root = loader.load();
        LoginController ctrl = loader.getController();
        ctrl.setService(new LoginService(loginRepository), primaryStage);

        primaryStage.setScene(new Scene(root, 450, 300));
        primaryStage.setTitle("Fakebook");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
