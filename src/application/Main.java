package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	
	public final static String SERVER_HOST = "localhost";
	public final static int SERVER_PORT = 6670;


    @Override
    public void start(Stage primaryStage) {
        LoginScreen login = new LoginScreen(primaryStage);
        Scene scene = new Scene(login.getView(), 800, 600);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
