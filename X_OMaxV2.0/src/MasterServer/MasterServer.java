package MasterServer;

import Commons.User;


import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;


public class MasterServer {
	protected Socket ss;
	protected static ServerSocket Mserver;
	protected int port;
	
	protected  int clientsNum=0;
	protected final static int maxServers = 4;


	protected ServerThread[] serverThreads = new ServerThread[maxServers];
	protected User[] knownActiveUsers = new User[200];
	
	protected static User masterServerUser;
	public static PublicKey pbKey;
	protected static PrivateKey prKey;
	protected Signature rsa;


	public static void main(String[] args) {
		int serverPort = 6500;

		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair pair = keyGen.generateKeyPair();
			pbKey = pair.getPublic();
			prKey = pair.getPrivate();
			masterServerUser = new User("master", pbKey);
			Mserver = new ServerSocket(serverPort);
			MasterServer s = new MasterServer();
			s.run();
		} catch (NoSuchAlgorithmException e1) {

			e1.printStackTrace();

		} catch (IOException e) {
			System.out.println(e);
		}

	}

	public void run() throws IOException {

		MasterServerConnectionHandler acceptConnection = new MasterServerConnectionHandler(this);
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
