package triathlon.client.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import triathlon.model.Participant;
import triathlon.model.Referee;
import triathlon.services.IService;

public class LoginController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;


    @FXML
    private Button loginButton;

    private IService server;

    private HomeController mainCtrl;
 //   private Referee crtReferee;
    private Parent mainParent;

    public void setMainParent(Parent mainParent) {
        this.mainParent = mainParent;
    }

    public void setServer(IService server){
        this.server = server;
    }


    @FXML
    void login(ActionEvent event) throws Exception {
        String username = usernameText.getText();
        String password = passwordText.getText();
//        System.out.println(username + " " + password);
        Referee referee = new Referee(0,username,password,"a","a","a");
//        System.out.println(crtReferee);
        Platform.runLater(()->{
            try{
                Referee connectedReferee = server.login(referee, mainCtrl);
                System.out.println("Logged Referee "+connectedReferee.getFirst_name());
                Stage stage=new Stage();
                stage.setTitle("Window for "+connectedReferee.getId()+")" + connectedReferee.getFirst_name() + " " + connectedReferee.getLast_name());
                stage.setScene(new Scene(mainParent));
                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        mainCtrl.logout();
                        System.exit(0);
                    }
                });
                mainCtrl.setActiveReferee(connectedReferee);
                mainCtrl.setServer(server);
                stage.show();
                ((Node)(event.getSource())).getScene().getWindow().hide();
//                PRIMESTE DATELE !!!!!
//                for(Participant p: server.getAllParticipants()){
//                    System.out.println(p);
//                }
            }  catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("MPP Triathlon");
                alert.setHeaderText("Authentication failure");
                alert.setContentText("Wrong username or password");
                alert.showAndWait();
            }
        });

    }

    private Stage initializeMainStage(Referee referee) throws Exception {
        mainCtrl.setActiveReferee(referee);
        Stage stage = new Stage();
        stage.setScene(new Scene(mainParent));
        return stage;
    }

    public void setHomeController(HomeController ctrl) {
        this.mainCtrl = ctrl;
    }


}
