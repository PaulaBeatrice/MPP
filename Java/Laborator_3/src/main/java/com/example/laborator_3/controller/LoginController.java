package com.example.laborator_3.controller;

import com.example.laborator_3.domain.Referee;
import com.example.laborator_3.service.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;


    @FXML
    private Button loginButton;

    private Service service;

    public LoginController(){

    }

    public void setService(Service service){
        this.service=service;
    }


    @FXML
    void login(ActionEvent event) throws IOException {
        //Service service = new Service();
        String username = usernameText.getText();
        String password = passwordText.getText();
        for(Referee referee: service.getAllReferees())
        {
            if(referee.getUsername().equals(username) && referee.getPassword().equals(password))
            {
                usernameText.clear();
                passwordText.clear();
                FXMLLoader fxmlLoader= new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/example/laborator_3/Home.fxml"));
                Parent root = fxmlLoader.load();
                HomeController ctrl = fxmlLoader.getController();
                ctrl.setService(service);
                ctrl.setActiveReferee(referee);
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Welcome" + referee.getLast_name());
                stage.show();

            }
        }
    }

}
