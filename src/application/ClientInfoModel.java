package application;

public class ClientInfoModel {

	private final static long MAX_TIME_ALIVE = 5000; // 5 SECOND

	private String name;
	private String ip;
	private int port;
	private long lastAlive;

	ClientInfoModel(String name, String ip, int port) {
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.lastAlive = System.currentTimeMillis();
	}

	ClientInfoModel(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public long getLastAlive() {
		return lastAlive;
	}

	public void setLastAlive(long lastAlive) {
		this.lastAlive = lastAlive;
	}

	public void refreshLastAlive() {
		this.lastAlive = System.currentTimeMillis();
	}

	public boolean isAlive() {
		return System.currentTimeMillis() - lastAlive < MAX_TIME_ALIVE;
	}

	@Override
	public String toString() {
		return "ClientInfoModel [name=" + name + ", ip=" + ip + ", port=" + port + ", lastAlive=" + lastAlive + "]";
	}

}
