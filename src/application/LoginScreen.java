package application;

import application.Client.RequestResult;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

public class LoginScreen {
	private VBox layout;
	private TextField nameField;
	private Label errorLabel;
	private ProgressIndicator loadingIndicator;
	private Button loginButton;

	public LoginScreen(Stage stage) {
		layout = new VBox(20);
		layout.setAlignment(Pos.CENTER);
		layout.setPadding(new Insets(40));
		layout.setBackground(new Background(new BackgroundFill(Color.web("#F0F8FF"), CornerRadii.EMPTY, Insets.EMPTY)));

		Label label = new Label("Enter your name:");
		label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
		label.setTextFill(Color.web("#1877F2"));

		nameField = new TextField();
		nameField.setMaxWidth(250);
		nameField.setFont(Font.font("Segoe UI", 15));
		nameField.setStyle("-fx-background-color: white; -fx-border-color: #1877F2; -fx-border-radius: 6;");

		errorLabel = new Label("");
		errorLabel.setTextFill(Color.RED);
		errorLabel.setFont(Font.font("Segoe UI", 14));
		errorLabel.setVisible(false);

		loadingIndicator = new ProgressIndicator();
		loadingIndicator.setVisible(false);
		loadingIndicator.setPrefSize(30, 30);

		loginButton = new Button("Login");
		loginButton.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
		loginButton.setTextFill(Color.WHITE);
		loginButton.setStyle("-fx-background-color: #1877F2; -fx-background-radius: 6;");

		loginButton.setOnAction(e -> {
			errorLabel.setVisible(false);
			String name = nameField.getText().trim();
			if (name.isEmpty()) {
				showError("Please enter your name.");
			} else {
				startLoginTask(name, stage);
			}
		});

		layout.getChildren().addAll(label, nameField, errorLabel, loadingIndicator, loginButton);
	}

	public VBox getView() {
		return layout;
	}

	private void showError(String message) {
		errorLabel.setText(message);
		errorLabel.setVisible(true);
	}

	private void startLoginTask(String name, Stage stage) {
		loginButton.setDisable(true);
		loadingIndicator.setVisible(true);

		Client.sendRegisterRequest(name, Main.SERVER_PORT, Main.SERVER_HOST, new RequestResult<>() {

			@Override
			public void onSuccessful(Boolean value) {
				System.out.println("onSuccessful");
				MessengerScreen messenger = new MessengerScreen(name);
				Scene scene = new Scene(messenger.getView(), 800, 600);
				stage.setScene(scene);
				stage.setTitle("Welcome, " + name);
				stage.centerOnScreen();
			}

			@Override
			public void onError(String msg) {
				System.out.println("onError:" + msg);
				showError(msg);
				loginButton.setDisable(false);
				loadingIndicator.setVisible(false);
			}
		});

	}

}
