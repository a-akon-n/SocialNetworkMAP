package com.example.controller;

import com.example.domain.Login;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.service.LoginService;

public class LoginController {
    @FXML
    private TextField userIdField;
    @FXML
    private TextField passwordField;

    private LoginService service;
    Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setService(LoginService service,  Stage stage) {
        this.service = service;
        this.dialogStage=stage;
    }

    public void handleLogin(){
        String userId = userIdField.getText();
        String password = passwordField.getText();

        if(service.passwordIsValid(new Login(Long.parseLong(userId), password))){
            service.setCurrentUserId(Long.parseLong(userId));
            // TODO: open menu
            System.out.println("Loged in successfuly!");
        } else {
            // TODO: show some message
        }
    }

    public void handleRegister(){
        Long userId = Long.parseLong(userIdField.getText());
        String password = passwordField.getText();

        service.addEntity(new Login(userId, password));
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
