package application;

public class MainServer {

	public static void main(String[] args) {
		
		DirectoryServer serverRunnable = new DirectoryServer(6670);
		Thread serverThread = new Thread(serverRunnable);
		serverThread.start();
		

	}

}
