package triathlon.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import triathlon.client.gui.HomeController;
import triathlon.client.gui.LoginController;
import triathlon.model.Participant;
import triathlon.network.rpcprotocol.TriathlonServicesRpcProxy;
import triathlon.services.IService;

import java.io.IOException;
import java.util.Properties;

public class StartClientRFC extends Application {
    private Stage primaryStage;

    private static int defaultPort = 55556;
    private static String defaultServer = "localhost";


    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClientRFC.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);


        IService server = new TriathlonServicesRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("Login.fxml"));
        Parent loginroot = loader.load();


        LoginController loginctrl =
                loader.<LoginController>getController();
        loginctrl.setServer(server);


        FXMLLoader mainloader = new FXMLLoader(
                getClass().getClassLoader().getResource("Home.fxml"));
        Parent mainroot = mainloader.load();


        HomeController homeCtrl =
                mainloader.<HomeController>getController();
        homeCtrl.setServer(server);

        loginctrl.setHomeController(homeCtrl);
        loginctrl.setMainParent(mainroot);
        //loginctrl.setPrimaryStage(primaryStage);

        primaryStage.setTitle("MPP home");
        primaryStage.setScene(new Scene(loginroot, 500, 300));
        primaryStage.show();

    }
}