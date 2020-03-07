package masterServer;

import Commons.SecretUser;
import Commons.User;
import SlaveServer.MasterConnectionManger;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;



public class MasterServer {
	protected Socket ss;
	protected static ServerSocket Mserver;

	protected  int clientsNum=0;
	protected final static int maxServers = 4;

	protected ServerThread[] serverThreads = new ServerThread[maxServers];
	protected User[] knownActiveUsers = new User[200];
	
	protected static SecretUser masterServerUser;



	public static void main(String[] args) {
		int serverPort = 6500;



		try {
			masterServerUser = SecretUser.generateSecretUser("MasterServer");
			Mserver = new ServerSocket(serverPort);
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
	

	
	public Boolean checkUserName(String usrName) {

		for(User u : knownActiveUsers)
			if(u!=null)
			if(usrName.equals(u.userName))
				return false;
		
		return true;
	}


}
