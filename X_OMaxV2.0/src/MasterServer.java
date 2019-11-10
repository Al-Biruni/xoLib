
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;




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

	Thread acceptConnection;

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
		acceptConnection = new Thread() {

			public void run() {

				while (true) {

					try {
						System.out.println("waiting for more servers");
						ss = Mserver.accept();
					} catch (IOException e) {

						e.printStackTrace();
					}

					if (ss != null) {
						System.out.println("SS not null");
						for (int serverNumber = 0; serverNumber < maxServers; serverNumber++) {
							if (serverThreads[serverNumber] == null) {
								System.out.println("Server conected......." + serverNumber);
								serverThreads[serverNumber] = new ServerThread(ss, serverNumber);
								System.out.println("Starting serverThread");
								serverThreads[serverNumber].start();
								System.out.println("Done");
								ss = new Socket();
								break;
							} else if (serverNumber == maxServers) {
								try {
									System.out.println("MaxServers reached");
									ss.close();
								} catch (IOException e) {

									e.printStackTrace();
								}
							}
						}

					}

				}
			}

		};
		
		acceptConnection.start();
	}
	

	
	public Boolean checkUserName(String usrName) {

		for(User u : knownActiveUsers)
			if(u!=null)
			if(usrName.equals(u.userName))
				return false;
		
		return true;
	}
	class ServerThread extends Thread {
		protected int serverNumber;
		protected Socket mySocket;
		protected ObjectOutputStream mO;
		protected ObjectInputStream mI;

		protected ServerThread[] runningServers = new ServerThread[maxServers];

		ServerThread(Socket ss, int serverNumber) {
			this.mySocket = ss;
			this.serverNumber = serverNumber;
			

			try {
				this.mO = new ObjectOutputStream(mySocket.getOutputStream());

				this.mI = new ObjectInputStream(mySocket.getInputStream());

			} catch (Exception e) {
				e.printStackTrace();
				//closeThread();
			}

		}

		public void run() {

			Message m = null;

			while (true) {
				try {

					m = (Message) mI.readObject();
					System.out.println("Master reciving: " + m.toString());

				} catch (EOFException e) {
					//continue;
					closeThread();
					break;
				} catch (IOException | ClassNotFoundException e) {

					this.closeThread();
					break;

				}
				if (m != null)
					if (m.TTL > 0) {
						MessageType reqType = m.messageType;
						System.out.println(m.toString());
						switch (reqType) {

						case GETALLUSERS:

							Message rr = new Message(masterServerUser,m.receiver,"",MessageType.ONLINEUSERS);
							User[] onU = new User[clientsNum];
							int i=0;
							for(User u : knownActiveUsers) {
								if(u!=null) {
									onU[i]=u;
									i++;
								}
							}
							rr.users=onU;
							
							System.out.println("sending memberlist");

							sendToServer(rr);

							break;

						case PUBLIC:
						case PRIVATE:
							m.TTL=4;
							sendToServers(m);

							break;
						//case ONLINEUSERS:  now master have the main and only member list no more rerouting 
							//sendToServers(m);
							//break;
						
						case REGISTER:Boolean av = checkUserName(m.sender.userName);
						if(av) {
						knownActiveUsers[clientsNum] = m.sender;
						Message r = new Message(masterServerUser,m.sender,"true",MessageType.REGISTER);
						r.users=m.users;
						System.err.println(r.users[0].toString());
						clientsNum++;
						sendToServer(r);
						sendToServers(r);
						}else {
							Message r = new Message(masterServerUser,m.sender,"false",MessageType.REGISTER);
							r.users=m.users;
							sendToServer(r);
						}break;
						
						case LOGOUT: logOut(m.msgBody);sendToServers(m);
						

						default:
							break;
						}
					}
			}

		}



		private synchronized void logOut(String outUserName) {
			clientsNum--;
			for(int i=0;i<knownActiveUsers.length;i++) {
				if(knownActiveUsers[i]!=null) {
					if(knownActiveUsers[i].userName.equals(outUserName)) {
						knownActiveUsers[i]=null;
					}
				}
			}
			
		}

		private synchronized  void sendToServers(Message msg) {
			System.out.println("sending to servers " + msg.toString());
			
				
				for (int i=0;i<serverThreads.length;i++)
					if (serverThreads[i] != null)
						if(serverThreads[i]!=this)

							serverThreads[i].sendToServer(msg);

			
			System.out.println("DOne sending to servers");
		}
		private void sendToServer(Message msg) {
			try {
				Message m = new Message(msg);
				m.decTTL();
				mO.writeObject(m);
				mO.flush();

			} catch (IOException e) {
				
				e.printStackTrace();
			}

		}

		private synchronized void closeThread() {

			try {
				mI.close();
				mO.close();
			

				for (int i = 0; i < runningServers.length; i++) {
					if (runningServers[i] == this) {
						runningServers[i] = null;
					}
				}
			} catch (IOException e) {
	
				e.printStackTrace();
				
			}

		}

	}


}
