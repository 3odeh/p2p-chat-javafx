//package application;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//
//import application.Client.RequestResult;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.control.*;
//import javafx.scene.layout.*;
//import javafx.scene.paint.Color;
//import javafx.scene.text.Font;
//import javafx.scene.text.FontWeight;
//
//public class MessengerScreen {
//	private BorderPane layout;
//	private ListView<String> clientList;
//	private ObservableList<String> observableList;
//	private TextArea chatArea;
//	private TextField messageInput;
//	private Label chatTitle;
//
//	private VBox chatBox;
//	private HBox messageBox;
//	private Button requestChatButton;
//
////	private final static HashMap<String, ChatSession> chatSessions = new HashMap<>();
//
//	public MessengerScreen(String username) {
//		layout = new BorderPane();
//		layout.setBackground(new Background(new BackgroundFill(Color.web("#F0F8FF"), CornerRadii.EMPTY, Insets.EMPTY)));
//
//		// ---------- LEFT SIDE ----------
//		Label clientsLabel = new Label("Clients");
//		clientsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//		clientsLabel.setPadding(new Insets(0, 0, 2, 0));
//
//		observableList = FXCollections.observableArrayList();
//		clientList = new ListView<>(observableList);
//		clientList.setPrefWidth(180);
//		clientList.setStyle("-fx-border-color: #1877F2; -fx-font-size: 14px;");
//		VBox.setVgrow(clientList, Priority.ALWAYS);
//
//		VBox leftBox = new VBox(4, clientsLabel, clientList);
//		leftBox.setPadding(new Insets(5));
//		layout.setLeft(leftBox);
//
//		// ---------- CENTER AREA ----------
//		chatTitle = new Label("Select a client to chat");
//		chatTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
//		chatTitle.setPadding(new Insets(0, 0, 5, 0));
//
//		chatArea = new TextArea();
//		chatArea.setEditable(false);
//		chatArea.setWrapText(true);
//		chatArea.setFont(Font.font("Segoe UI", 14));
//		chatArea.setStyle("-fx-control-inner-background: white; -fx-font-size: 14px; -fx-border-color: #1877F2;");
//		VBox.setVgrow(chatArea, Priority.ALWAYS);
//
//		messageInput = new TextField();
//		messageInput.setPromptText("Type your message...");
//		messageInput.setFont(Font.font("Segoe UI", 14));
//		messageInput.setMaxWidth(Double.MAX_VALUE);
//
//		Button sendButton = new Button("Send");
//		sendButton.setFont(Font.font("Segoe UI", 14));
//
//		sendButton.setOnAction(e -> sendMsg());
//		messageInput.setOnAction(e -> sendMsg());
//
//		messageBox = new HBox(10, messageInput, sendButton);
//		HBox.setHgrow(messageInput, Priority.ALWAYS);
//
//		chatBox = new VBox(6);
//		chatBox.setPadding(new Insets(5));
//		chatBox.getChildren().add(chatTitle);
//
//		layout.setCenter(chatBox);
//
//		// ---------- CLIENT SELECTION ----------
//		clientList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//			chatBox.getChildren().clear();
//			if (newVal != null) {
//				chatTitle.setText("Chat with " + newVal);
//				chatBox.getChildren().add(chatTitle);
//
//				if (Client.getInstance().isHaveSession(newVal)) {
//					chatArea.setText(Client.getInstance().getSessionContent(newVal));
//					chatBox.getChildren().addAll(chatArea, messageBox);
//				} else {
//					requestChatButton = new Button("Request Chat");
//					requestChatButton.setFont(Font.font("Segoe UI", 14));
//					requestChatButton.setOnAction(e -> startChatRequest(newVal));
//					chatBox.getChildren().add(requestChatButton);
//				}
//			}
//		});
//
//		// ---------- CLIENT LIST UPDATER ----------
//		Client.getInstance().startClientActive(new RequestResult<String[]>() {
//			@Override
//			public void onSuccessful(String[] value) {
//				Platform.runLater(() -> {
//					String selectedClient = clientList.getSelectionModel().getSelectedItem();
//					List<String> newList = List.of(value);
//
//					if (!observableList.equals(newList)) {
//						observableList.setAll(newList);
//						if (selectedClient != null && newList.contains(selectedClient)) {
//							clientList.getSelectionModel().select(selectedClient);
//						}
//					}
//				});
//			}
//
//			@Override
//			public void onError(String msg) {
//
//			}
//		});
//
//		Client.getInstance().serverMsgListener(new RequestResult<ChatSession>() {
//
//			@Override
//			public void onSuccessful(ChatSession value) {
//				
//				if (chatSessions.containsKey(value.getUserName())) {
//					Platform.runLater(() -> {
//						ChatSession session = chatSessions.get(value.getUserName());
//						session.appendMessage(value.getContent());
//
//						if (clientList.getSelectionModel().getSelectedItem() != null
//								&& clientList.getSelectionModel().getSelectedItem().equals(value.getUserName())) {
//							chatArea.setText(session.getContent());
//
//						}
//					});
//				} else {
//					Platform.runLater(() -> {
//						chatSessions.put(value.getUserName(), value);
//
//						if (clientList.getSelectionModel().getSelectedItem() != null
//								&& clientList.getSelectionModel().getSelectedItem().equals(value.getUserName())) {
//							chatArea.setText(value.getContent());
//						}
//					});
//				}
//			}
//
//			@Override
//			public void onError(String msg) {
//				// TODO Auto-generated method stub
//
//			}
//		});
//	}
//
//	private void startChatRequest(String user) {
//		chatBox.getChildren().remove(requestChatButton);
//		Label loading = new Label("Requesting connection...");
//		chatBox.getChildren().add(loading);
//
//		Client.getInstance().sendTalkRequest(user, new RequestResult<ChatSession>() {
//
//			@Override
//			public void onSuccessful(ChatSession session) {
//				Platform.runLater(() -> {
//					chatSessions.put(user, session);
//					chatBox.getChildren().remove(loading);
//					chatArea.clear();
//					chatBox.getChildren().addAll(chatArea, messageBox);
//				});
//			}
//
//			@Override
//			public void onError(String msg) {
//				// TODO Auto-generated method stub
//				loading.setText(msg);
//				loading.setTextFill(Color.RED);
//
//			}
//		});
//
//	}
//
//	private void sendMsg() {
//		String msg = messageInput.getText();
//		if (!msg.isEmpty()) {
//			String selectedClient = clientList.getSelectionModel().getSelectedItem();
//			if (selectedClient != null && chatSessions.containsKey(selectedClient)) {
//				ChatSession session = chatSessions.get(selectedClient);
//				String fullMessage = "You:" + msg;
//				session.appendMessage(fullMessage);
//				chatArea.appendText(fullMessage + "\n");
//				messageInput.clear();
//				session.setBufferMsg(msg);
//			}
//		}
//	};
//
//	public BorderPane getView() {
//		return layout;
//	}
//}
package application;
 
import java.util.List;

import application.Client.RequestResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class MessengerScreen {
	private BorderPane layout;
	private ListView<String> clientList;
	private ObservableList<String> observableList;
	private TextArea chatArea;
	private TextField messageInput;
	private Label chatTitle;

	private VBox chatBox;
	private HBox messageBox;
	private Button requestChatButton;

//	private final static HashMap<String, ChatSession> chatSessions = new HashMap<>();

	public MessengerScreen(String username) {
		layout = new BorderPane();
		layout.setBackground(new Background(new BackgroundFill(Color.web("#F0F8FF"), CornerRadii.EMPTY, Insets.EMPTY)));

		// ---------- LEFT SIDE ----------
		Label clientsLabel = new Label("Clients");
		clientsLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
		clientsLabel.setPadding(new Insets(0, 0, 2, 0));

		observableList = FXCollections.observableArrayList();
		clientList = new ListView<>(observableList);
		clientList.setPrefWidth(180);
		clientList.setStyle("-fx-border-color: #1877F2; -fx-font-size: 14px;");
		VBox.setVgrow(clientList, Priority.ALWAYS);

		VBox leftBox = new VBox(4, clientsLabel, clientList);
		leftBox.setPadding(new Insets(5));
		layout.setLeft(leftBox);

		// ---------- CENTER AREA ----------
		chatTitle = new Label("Select a client to chat");
		chatTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
		chatTitle.setPadding(new Insets(0, 0, 5, 0));

		chatArea = new TextArea();
		chatArea.setEditable(false);
		chatArea.setWrapText(true);
		chatArea.setFont(Font.font("Segoe UI", 14));
		chatArea.setStyle("-fx-control-inner-background: white; -fx-font-size: 14px; -fx-border-color: #1877F2;");
		VBox.setVgrow(chatArea, Priority.ALWAYS);

		messageInput = new TextField();
		messageInput.setPromptText("Type your message...");
		messageInput.setFont(Font.font("Segoe UI", 14));
		messageInput.setMaxWidth(Double.MAX_VALUE);

		Button sendButton = new Button("Send");
		sendButton.setFont(Font.font("Segoe UI", 14));

		sendButton.setOnAction(e -> sendMsg());
		messageInput.setOnAction(e -> sendMsg());

		messageBox = new HBox(10, messageInput, sendButton);
		HBox.setHgrow(messageInput, Priority.ALWAYS);

		chatBox = new VBox(6);
		chatBox.setPadding(new Insets(5));
		chatBox.getChildren().add(chatTitle);

		layout.setCenter(chatBox);

		// ---------- CLIENT SELECTION ----------
		clientList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			chatBox.getChildren().clear();
			if (newVal != null) {
				chatTitle.setText("Chat with " + newVal);
				chatBox.getChildren().add(chatTitle);

				if (Client.getInstance().isHaveSession(newVal)) {
					chatArea.setText(Client.getInstance().getSessionContent(newVal));
					chatBox.getChildren().addAll(chatArea, messageBox);
				} else {
					requestChatButton = new Button("Request Chat");
					requestChatButton.setFont(Font.font("Segoe UI", 14));
					requestChatButton.setOnAction(e -> startChatRequest(newVal));
					chatBox.getChildren().add(requestChatButton);
				}
			}
		});

		// ---------- CLIENT LIST UPDATER ----------
		Client.getInstance().startClientActive(new RequestResult<String[]>() {
			@Override
			public void onSuccessful(String[] value) {
				Platform.runLater(() -> {
					String selectedClient = clientList.getSelectionModel().getSelectedItem();
					List<String> newList = List.of(value);

					if (!observableList.equals(newList)) {
						observableList.setAll(newList);
						if (selectedClient != null && newList.contains(selectedClient)) {
							clientList.getSelectionModel().select(selectedClient);
						}
					}
				});
			}

			@Override
			public void onError(String msg) {

			}
		});

		Client.getInstance().serverMsgListener(new RequestResult<ChatSession>() {

			@Override
			public void onSuccessful(ChatSession value) {

				Platform.runLater(() -> {
					if (clientList.getSelectionModel().getSelectedItem() != null
							&& clientList.getSelectionModel().getSelectedItem().equals(value.getUserName())) {
						chatArea.setText(value.getContent());
					}
				});
			}

			@Override
			public void onError(String msg) {
			}
		});
	}

	private void startChatRequest(String user) {
		chatBox.getChildren().remove(requestChatButton);
		Label loading = new Label("Requesting connection...");
		chatBox.getChildren().add(loading);

		Client.getInstance().sendTalkRequest(user, new RequestResult<ChatSession>() {

			@Override
			public void onSuccessful(ChatSession session) {
				Platform.runLater(() -> {
					chatBox.getChildren().remove(loading);
					chatArea.clear();
					chatBox.getChildren().addAll(chatArea, messageBox);
				});
			}

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				loading.setText(msg);
				loading.setTextFill(Color.RED);

			}
		}, new RequestResult<ChatSession>() {

			@Override
			public void onSuccessful(ChatSession value) {

				Platform.runLater(() -> {
					if (clientList.getSelectionModel().getSelectedItem() != null
							&& clientList.getSelectionModel().getSelectedItem().equals(value.getUserName())) {
						chatArea.setText(value.getContent());
					}
				});
			}

			@Override
			public void onError(String msg) {
			}
		});

	}

	private void sendMsg() {
		String msg = messageInput.getText();
		if (!msg.isEmpty()) {
			String selectedClient = clientList.getSelectionModel().getSelectedItem();
			if (selectedClient != null) {
				Client.getInstance().sendMsg(selectedClient, msg);
				String fullMessage = "You:" + msg;
				chatArea.appendText(fullMessage + "\n");
				messageInput.clear();
			}
		}
	};

	public BorderPane getView() {
		return layout;
	}
}
