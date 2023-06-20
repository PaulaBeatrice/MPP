package com.example.laborator_3.controller;

import com.example.laborator_3.domain.Participant;
import com.example.laborator_3.domain.Referee;
import com.example.laborator_3.domain.Result;
import com.example.laborator_3.service.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class HomeController {
    Referee connectedReferee;

    void setActiveReferee(Referee connectedReferee) {
        this.connectedReferee = connectedReferee;
        init();
    }

    private Service service;

    public HomeController(){

    }

    public void setService(Service service){
        this.service=service;
    }

    void init(){

        first_nameCollumnNotNoted.setCellValueFactory(new PropertyValueFactory<Participant,String>("first_name"));
        last_nameCollumnNotNoted.setCellValueFactory(new PropertyValueFactory<Participant,String>("last_name"));
        idNotNotedCollumn.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("id"));

        List<Participant> notedParticipants = service.getEvaluatedParticipants(connectedReferee);

        for(Participant participant: service.getAllParticipants())
        {
            boolean containsParticipant = false;
            for(Participant p: notedParticipants)
                if(p.getId().equals(participant.getId()))
                    containsParticipant = true;
            if(!containsParticipant)
                notNoted.add(participant);
        }

        refereeNotNotedTable.setItems(notNoted);



        first_nameCollumn.setCellValueFactory(new PropertyValueFactory<Participant,String>("first_name"));
        last_nameCollumn.setCellValueFactory(new PropertyValueFactory<Participant,String>("last_name"));
        idCollumn.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("id"));
        pointsCollumn.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("points"));
        for(Participant participant:service.getTopParticipants(connectedReferee.getActivity()))
            top.add(participant);

        Comparator<Participant> participantComparatorComparator = Comparator.comparing(Participant::getPoints).reversed();
        Collections.sort(top, participantComparatorComparator);
        refereeTable.setItems(top);


        f_namePR.setCellValueFactory(new PropertyValueFactory<Participant,String>("first_name"));
        l_namePr.setCellValueFactory(new PropertyValueFactory<Participant,String>("last_name"));
        idTopPr.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("id"));
        pointsPr.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("points"));
        for(Participant participant:service.getTopParts(connectedReferee))
            topPR.add(participant);

        Comparator<Participant> participantComparator = Comparator.comparing(Participant::getFirst_name);
        Collections.sort(topPR, participantComparator);
        tabelTopPr.setItems(topPR);
    }


    @FXML
    private TableView<Participant> refereeNotNotedTable;

    @FXML
    private TableColumn<Participant, Integer> idNotNotedCollumn;

    @FXML
    private TableColumn<Participant, String> first_nameCollumnNotNoted;

    @FXML
    private TableColumn<Participant, String> last_nameCollumnNotNoted;

    @FXML
    private TextField scoreText;

    @FXML
    private TableView<Participant> refereeTable;

    @FXML
    private TableColumn<Participant, Integer> idCollumn;

    @FXML
    private TableColumn<Participant, String> first_nameCollumn;

    @FXML
    private TableColumn<Participant, String> last_nameCollumn;

    @FXML
    private TableColumn<Participant, Integer> pointsCollumn;

    @FXML
    private TableView<Participant> tabelTopPr;


    @FXML
    private TableColumn<Participant, Integer> idTopPr;

    @FXML
    private TableColumn<Participant, String> f_namePR;

    @FXML
    private TableColumn<Participant, String> l_namePr;

    @FXML
    private TableColumn<Participant, Integer> pointsPr;


    ObservableList<Participant> notNoted = FXCollections.observableArrayList();
    ObservableList<Participant> top = FXCollections.observableArrayList();

    ObservableList<Participant> topPR = FXCollections.observableArrayList();

    ObservableList<Map<Participant,Integer>> notNotedParticipants = FXCollections.observableArrayList();


    @FXML
    void addScore(MouseEvent event) {
        Participant participant = service.getParticipantById(refereeNotNotedTable.getSelectionModel().getSelectedItem().getId());
        int points = Integer.parseInt(scoreText.getText());
        String activity = connectedReferee.getActivity();

        service.addScore(new Result(1,participant,connectedReferee,activity,points));

        scoreText.clear();
        notNoted.clear();
        top.clear();
        init();
    }

    @FXML
    void chosenReferee(MouseEvent event) {

    }

    @FXML
    private Button logoutButton;

    @FXML
    void logout(MouseEvent event) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }
}
