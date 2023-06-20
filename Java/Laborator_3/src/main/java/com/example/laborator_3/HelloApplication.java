package com.example.laborator_3;

import com.example.laborator_3.controller.LoginController;
import com.example.laborator_3.repository.ParticipantDBRepository;
import com.example.laborator_3.repository.RefereeDBRepository;
import com.example.laborator_3.repository.ResultDBRepository;
import com.example.laborator_3.service.Service;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Properties props=new Properties();
        try {
            // C:\FACULTATE\Anul 2\Semestrul 2\Medii de Proiectare si Programare\Laboratoare\mpp-proiect-java-PaulaBeatrice\Laborator_3\
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        RefereeDBRepository refereeDBRepository=new RefereeDBRepository(props);
        ParticipantDBRepository participantDBRepository = new ParticipantDBRepository(props);
        ResultDBRepository resultDBRepository = new ResultDBRepository(props,refereeDBRepository,participantDBRepository);
        Service service = new Service(refereeDBRepository,participantDBRepository,resultDBRepository);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Parent root = loader.load();
            LoginController ctrl = loader.getController();
            ctrl.setService(service);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Hello");
            stage.show();
        }catch(Exception e){
            System.out.println(e);
        }
    }


    public static void main(String[] args) {
        launch();
    }
}