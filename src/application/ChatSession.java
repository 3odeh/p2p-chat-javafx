package application;

import java.io.PrintWriter;
import java.net.Socket;

public class ChatSession {
	private String userName;
	private String id;
	private int port;
	private String content;

	private Socket socket;
	private PrintWriter printWriter;

	public ChatSession(String userName, String id, int port, Socket socket, PrintWriter printWriter) {
		this.userName = userName;
		this.id = id;
		this.port = port;
		this.content = "";
		this.socket = socket;
		this.printWriter = printWriter;
	}

	public String getUserName() {
		return userName;
	}

	public String getId() {
		return id;
	}

	public int getPort() {
		return port;
	}

	public String getContent() {
		return content;
	}

	public Socket getSocket() {
		return socket;
	}

	public PrintWriter getPrintWriter() {
		return printWriter;
	}

	public void appendMessage(String msg) {
		content += msg + "\n";
	}

}
