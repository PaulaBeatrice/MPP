package triathlon.client.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.model.Result;
import triathlon.services.IObserver;
import triathlon.services.IService;

import java.net.URL;
import java.sql.Ref;
import java.util.*;

public class HomeController implements Initializable, IObserver {
    private Referee connectedReferee;

    public HomeController(){
        System.err.println("Constructor controller");
    }

    void setActiveReferee(Referee connectedReferee) throws Exception {
        this.connectedReferee = connectedReferee;
        initTabelNotNotedPart();
        initTableNotedPart();
        initTabelParts();
    }

    private IService server;

    public HomeController(IService server){
        this.server = server;
        System.err.println("constructor HomeController cu server param");
    }

    public void setServer(IService s) throws Exception {
        this.server=s;
    }

    void logout() {
        try {
            server.logout(connectedReferee.getId(), this);
        } catch (Exception e) {
            System.out.println("Logout error " + e);
        }

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
    void addScore(MouseEvent event) throws Exception {
        int index = refereeNotNotedTable.getSelectionModel().getSelectedIndex();
        if (index<0) Util.showWarning("Score has no specified participant","Please select a participant from the list");
        Participant participant = refereeNotNotedTable.getSelectionModel().getSelectedItem();
        if(scoreText.getText().isEmpty()) Util.showWarning("Score empty","Please fill in the score field before adding");

        int points = Integer.parseInt(scoreText.getText());
        try {
            Result res = new Result(1,participant,connectedReferee,connectedReferee.getActivity(),points);
            server.addScore(res);
            scoreText.clear();
            initTabelNotNotedPart();
            initTableNotedPart();

        } catch (Exception e) {
            Util.showWarning("Communication error","Your server probably closed connection");
        }
    }

    @FXML
    private Button logoutButton;

    @FXML
    void logout(MouseEvent event) {
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @Override
    public void notifyAddedPoints(Participant part) throws Exception {
        System.err.println("NotifyAdded " + part);
        Platform.runLater(() -> {
            ObservableList<Participant> participants = refereeTable.getItems();
            for (Participant participant : participants) {
                if (Objects.equals(participant.getId(), part.getId())) {
                    System.err.println("NotifyAdded modificare pentru "+participant);
                    int index = participants.indexOf(participant);
                    refereeTable.getItems().set(index, part);
                    refereeTable.getItems().sort(Comparator.comparingInt(Participant::getPoints).reversed()); // sortam dupa puncte
                    refereeTable.refresh();
                    break;
                }
            }
        });
    }


    public void presslogout(MouseEvent mouseEvent) {
        logout();
        ((Node)(mouseEvent.getSource())).getScene().getWindow().hide();
    }


    public void initTabelNotNotedPart(){
        // NOT NOTED BY REFEREE
        notNoted.clear();
        first_nameCollumnNotNoted.setCellValueFactory(new PropertyValueFactory<Participant,String>("first_name"));
        last_nameCollumnNotNoted.setCellValueFactory(new PropertyValueFactory<Participant,String>("last_name"));
        idNotNotedCollumn.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("id"));
        try {
            for(Participant p: server.getNotNoted(connectedReferee))
                notNoted.add(p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        refereeNotNotedTable.setItems(notNoted);
    }

    public void initTableNotedPart(){
        // NOTED BY REFEREE ORDER BY NAME
        topPR.clear();
        f_namePR.setCellValueFactory(new PropertyValueFactory<Participant,String>("first_name"));
        l_namePr.setCellValueFactory(new PropertyValueFactory<Participant,String>("last_name"));
        idTopPr.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("id"));
        pointsPr.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("points"));
        try {
            for(Participant participant:server.getNotedParticipants(connectedReferee))
                topPR.add(participant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Comparator<Participant> participantComparator = Comparator.comparing(Participant::getFirst_name);
        Collections.sort(topPR, participantComparator);
        tabelTopPr.setItems(topPR);
    }

    public void initTabelParts(){
        // TOP PARTICIPANTS ORDER BY SCORE DESC
        first_nameCollumn.setCellValueFactory(new PropertyValueFactory<Participant,String>("first_name"));
        last_nameCollumn.setCellValueFactory(new PropertyValueFactory<Participant,String>("last_name"));
        idCollumn.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("id"));
        pointsCollumn.setCellValueFactory(new PropertyValueFactory<Participant,Integer>("points"));
        try {
            for(Participant participant:server.getAllParticipants())
                top.add(participant);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Comparator<Participant> participantComparatorByPoints = Comparator.comparing(Participant::getPoints).reversed();
        Collections.sort(top, participantComparatorByPoints);
        refereeTable.setItems(top);
    }
}
