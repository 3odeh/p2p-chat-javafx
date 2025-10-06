package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class DirectoryServer implements Runnable {

	private final static String USER_REGISTER_MSG_HEADER = "I want to register";
	private final static String USER_LIVE_MSG_HEADER = "I am alive";
	private final static String USER_CONNECT_REQUEST_MSG_HEADER = "I want to talk";

	private HashMap<String, ClientInfoModel> clients = new HashMap<String, ClientInfoModel>();
	private int serverPortNumber;

	public DirectoryServer(int serverPortNumber) {
		super();
		this.serverPortNumber = serverPortNumber;
	}

	@Override
	public void run() {
		System.out.println("Server: start");

		getMsgsThreadRun();
	}

	private void getMsgsThreadRun() {

		try (ServerSocket serverSocket = new ServerSocket(serverPortNumber)) {
			Socket socket;

			while ((socket = serverSocket.accept()) != null) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String line;

				while ((line = in.readLine()) != null)
					try {
						System.out.println("Server:" + line);
						String[] parts = line.split(",");
						if (parts[0].equals(USER_REGISTER_MSG_HEADER)) {
							handleRegisterRequest(parts[1], socket.getInetAddress().getHostAddress(), socket);

						} else if (parts[0].equals(USER_LIVE_MSG_HEADER)) {
							handleLiveMsg(parts[1], socket.getInetAddress().getHostAddress(), parts[2], socket);

						} else if (parts[0].equals(USER_CONNECT_REQUEST_MSG_HEADER)) {
							handleUserTalkRequest(parts[1], socket);

						} else {
							System.out.println("Server Error: unknown message!");
						}

					} catch (Exception e) {
						System.out.println("Server Error:" + e.getMessage());
					}

			}
		} catch (IOException e) {
			System.out.println("Server Error:" + e.getMessage());
		}

	}

	private void handleRegisterRequest(String userName, String ip, Socket socket) throws IOException {
		String msg;
		if (clients.containsKey(userName) && clients.get(userName).isAlive()) {
			msg = "Error: user is already exist";
		} else {
			msg = "Successfull register";
			clients.put(userName, new ClientInfoModel(userName, ip));
		}
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		System.out.println("Server:" + msg);
		out.println(msg);

	}

	private void handleLiveMsg(String name, String ip, String port, Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		if (clients.containsKey(name)) {
			ClientInfoModel clientInfoModel = clients.get(name);
			clientInfoModel.refreshLastAlive();
			clientInfoModel.setPort(Integer.parseInt(port));

			String userList = "";

			for (var entry : clients.entrySet()) {
				if (entry.getValue().isAlive() && !entry.getValue().getName().equals(name)) {
					userList += entry.getValue().getName() + ",";
				}
			}
			out.println(userList);
		} else {
			out.print("Error: you are not register");
		}
	}

	private void handleUserTalkRequest(String name, Socket socket) throws IOException {
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

		if (clients.containsKey(name)) {
			ClientInfoModel clientInfoModel = clients.get(name);
			if (clientInfoModel.isAlive()) {
				String userInfo = clientInfoModel.getName() + "," + clientInfoModel.getIp() + ","
						+ clientInfoModel.getPort();
				out.println(userInfo);
			} else {
				out.println("Error: user offline");
			}
		} else {
			out.println("Error: user not found");
		}
	}
}
