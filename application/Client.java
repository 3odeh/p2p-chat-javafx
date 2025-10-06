package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Client {
	public interface RequestResult<T> {

		public void onSuccessful(T value);

		public void onError(String msg);
	}

	private final static String USER_REGISTER_MSG_HEADER = "I want to register";
	private final static String USER_LIVE_MSG_HEADER = "I am alive";
	private final static String USER_CONNECT_REQUEST_MSG_HEADER = "I want to talk";

	private static Client currentInstance;

	private int serverPortNumber;
	private String serverHost;

	private int localPortNumber;
	private String username;

	private final static HashMap<String, ChatSession> chatSessions = new HashMap<>();

	private Client(String username, int serverPortNumber, String serverHost) {
		super();
		this.username = username;
		this.serverPortNumber = serverPortNumber;
		this.serverHost = serverHost;

	}

	public static Client getInstance() {
		return currentInstance;
	}

	public static void sendRegisterRequest(String username, int serverPortNumber, String serverHost,
			RequestResult<Boolean> result) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try (Socket socket = new Socket(serverHost, serverPortNumber);
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
					out.println(USER_REGISTER_MSG_HEADER + "," + username);

					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = in.readLine();
					if (message.startsWith("Error")) {
						result.onError(message);
					} else {
						currentInstance = new Client(username, serverPortNumber, serverHost);
						result.onSuccessful(true);
					}
					System.out.println("Goood");
				} catch (IOException e) {
					result.onError("Server not available");
				}
			}
		});

		thread.run();
	}

	public void startClientActive(RequestResult<String[]> result) {
		sendMsgPeriodic(result);
	}

	public boolean isHaveSession(String username) {
		return chatSessions.containsKey(username);
	}

	public String getSessionContent(String username) {
		return chatSessions.get(username).getContent();
	}

	public void sendMsg(String username, String msg) {
		if (isHaveSession(username)) {

			ChatSession clientChatSession = chatSessions.get(username);
			if (clientChatSession.getSocket() != null && clientChatSession.getPrintWriter() != null) {
				System.out.println("sending:" + msg);
				System.out.println("Me:" + clientChatSession.getSocket().getLocalPort());
				System.out.println("To:" + clientChatSession.getSocket().getPort() + ","
						+ clientChatSession.getSocket().getInetAddress().getHostAddress());
				clientChatSession.appendMessage("You:" + msg);
				clientChatSession.getPrintWriter().println(username + "," + msg);
			} else {
				// chatSessions.remove(username);
			}
		}
	}

	private void sendMsgPeriodic(RequestResult<String[]> result) {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				if (!isFinishInitListener)
					return;
				try (Socket socket = new Socket(serverHost, serverPortNumber);
						PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

					out.println(USER_LIVE_MSG_HEADER + "," + username + "," + localPortNumber);

					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					String message = in.readLine();

					if (message.startsWith("Error")) {
						result.onError(message);
						timer.cancel();
					} else {
						String[] users = message.split(",");
						result.onSuccessful(users);
					}

				} catch (IOException e) {
					result.onError("Server not available");
					timer.cancel();
				}
			}
		}, 0, 1000);

	}

	public void sendTalkRequest(String name, RequestResult<ChatSession> result,
			RequestResult<ChatSession> listenerRec) {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try (Socket socket = new Socket(serverHost, serverPortNumber)) {
					PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
					out.println(USER_CONNECT_REQUEST_MSG_HEADER + "," + name);

					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String message = in.readLine();
					if (message.startsWith("Error")) {
						result.onError(message);
					} else {
						String[] user = message.split(",");
						Socket newSocket = new Socket(user[1], Integer.parseInt(user[2]));
						PrintWriter newPrintWriter = new PrintWriter(newSocket.getOutputStream(), true);
						newPrintWriter.println(username + ",start chat");
						ChatSession chatSession = new ChatSession(user[0], user[1], Integer.parseInt(user[2]),
								newSocket, newPrintWriter);
						chatSessions.put(chatSession.getUserName(), chatSession);
						result.onSuccessful(chatSession);

						new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									Socket sTmp = chatSession.getSocket();
									BufferedReader in = new BufferedReader(
											new InputStreamReader(sTmp.getInputStream()));

									while (true) {
										try {
											String newLine = in.readLine();
											String[] msgData = newLine.split(",", 2);
											String name = msgData[0];
											String content = msgData[1];
											chatSession.appendMessage(name + ":" + content);
											listenerRec.onSuccessful(chatSession);

											System.out.println("rec(" + newLine + ")");
											System.out.println("Me:" + chatSession.getSocket().getLocalPort());
											System.out.println("To:" + chatSession.getSocket().getPort() + ","
													+ chatSession.getSocket().getInetAddress().getHostAddress());
										} catch (IOException e) {
											e.printStackTrace();
										}
									}

								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				} catch (IOException e) {
					result.onError("User not available, please try again");
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		thread.run();
	}

	private ServerSocket serverSocket;
	private Socket socket;
	private boolean isFinishInitListener = false;;

	public void serverMsgListener(RequestResult<ChatSession> result) {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					try {
						localPortNumber = findRandomOpenPort();
						serverSocket = new ServerSocket(localPortNumber);
						if (!isFinishInitListener)
							isFinishInitListener = true;

						socket = serverSocket.accept();

						new Thread(new Runnable() {

							@Override
							public void run() {

								try {
									Socket sTmp = socket;
									BufferedReader in = new BufferedReader(
											new InputStreamReader(sTmp.getInputStream()));

									PrintWriter out = new PrintWriter(sTmp.getOutputStream(), true);

									String line = in.readLine();

									String[] clientMsg = line.split(",", 2);
									final String clientUsername = clientMsg[0];
									String clientMsgContent = clientMsg[1];

									ChatSession clientChatSession = new ChatSession(clientUsername,
											sTmp.getInetAddress().getHostAddress(), sTmp.getPort(), sTmp, out);

									clientChatSession.appendMessage(clientUsername + ":" + clientMsgContent);
									chatSessions.put(clientUsername, clientChatSession);

									result.onSuccessful(clientChatSession);

									while (true) {
										try {
											String newLine = in.readLine();
											String[] msgData = newLine.split(",", 2);
											String name = msgData[0];
											String content = msgData[1];
											clientChatSession.appendMessage(name + ":" + content);
											result.onSuccessful(clientChatSession);

											System.out.println("rec(" + newLine + ")");
											System.out.println("Me:" + clientChatSession.getSocket().getLocalPort());
											System.out.println("To:" + clientChatSession.getSocket().getPort() + ","
													+ clientChatSession.getSocket().getInetAddress().getHostAddress());
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
 
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}).start();

					} catch (IOException e) {
						System.out.println("Server Error:" + e.getMessage());
					}

				}
			}
		});

		thread.start();
	}

	private Integer findRandomOpenPort() throws IOException {
		try (ServerSocket socket = new ServerSocket(0);) {
			return socket.getLocalPort();
		}
	} 
 

}