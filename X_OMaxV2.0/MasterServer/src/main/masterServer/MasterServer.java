<<<<<<< HEAD:X_OMaxV2.0/MasterServer/src/main/java/MasterServer/MasterServer.java
package MasterServer;
=======
package masterServer;

import Commons.SecretUser;
import Commons.User;
>>>>>>> 849689989595f5a55e0e97aa0b3d48b1c88050da:X_OMaxV2.0/MasterServer/src/main/masterServer/MasterServer.java

import xoLib.SecretUser;
import xoLib.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class MasterServer {
	int serverPort = 6500;
	protected Socket ss;
	protected static ServerSocket Mserver;

	protected  int clientsNum=0;
	protected final static int maxServers = 4;

	protected ServerThread[] serverThreads = new ServerThread[maxServers];
	protected User[] knownActiveUsers = new User[200];
	
	protected static SecretUser masterServerUser;

public MasterServer() throws Exception {
	this.masterServerUser = SecretUser.generateSecretUser("MasterServer");

	Mserver = new ServerSocket(serverPort);
	System.out.println(Mserver.getLocalSocketAddress().toString());
	System.out.println(Mserver.getInetAddress().toString());


}
	public static void main(String[] args) {
		try {

			MasterServer s = new MasterServer();
			s.run();
		}catch (Exception e) {
			System.err.println("Couldn't initialize master server ");
			e.printStackTrace();
		}

	}

	public void run() throws IOException {

		MasterServerConnectionListener acceptConnection = new MasterServerConnectionListener(this);
		acceptConnection.start();
	}
	

	
	public  Boolean checkUserName(String usrName) {

		for(User u : knownActiveUsers)
			if(u!=null)
			if(usrName.equals(u.userName))
				return false;
		
		return true;
	}


}
