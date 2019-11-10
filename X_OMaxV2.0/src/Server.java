
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;

public class Server {

	protected int clientsNum;
	protected Socket newClientS;
	protected ServerSocket mySocket;
	protected Socket masterS;

	protected ObjectOutputStream mO;
	protected ObjectInputStream mI;

	protected final static int maxClients = 50;

	protected static User serverUser;

	public static PublicKey pbKey;
	protected static PrivateKey prKey;
	protected Signature rsa;
	Thread masterHandler, serverConnector, updateActiveUsers;

	protected ClientThread[] activeClients = new ClientThread[maxClients];

	protected User[] knownActiveUsers;

	// ------------------------constructor and main------------------------------
	public Server() {

		clientsNum = 0;
		int serverPort = 6500;
		int cPort = 6000;

		try {
			masterS = new Socket("localhost", serverPort);
			mySocket = new ServerSocket(cPort);
			newClientS = new Socket();

			mO = new ObjectOutputStream(masterS.getOutputStream());
			mI = new ObjectInputStream(masterS.getInputStream());

			System.out.println(
					"Connected to Master Server on port " + serverPort + "Waiting for clients on port " + cPort);

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void run() throws IOException {

		serverConnector = new Thread() {
			public void run() {
				while (true) {

					try {
						newClientS = mySocket.accept();
					} catch (IOException e) {
						System.err.println("Client Socket Error while accepting ");
						e.printStackTrace();
					}
					if (newClientS != null) {

						System.out.println("COnnecting creating new THread");
						activeClients[clientsNum] = new ClientThread(clientsNum, newClientS);
						activeClients[clientsNum].start();
						clientsNum++;
						newClientS = new Socket();

					}

				}

			}
		};

		masterHandler = new Thread() {

			public void run() {
				Message masterReq;
				while (true) {

					try {// check for master
						masterReq = (Message) mI.readObject();
						if (masterReq != null)
							if (masterReq.TTL > 0) {

								System.out.println("MAster Server request: " + masterReq.toString());

								switch (masterReq.messageType) {

								case GETALLUSERS:
									User[] ut = getActiveUsers();
									Message mReply = new Message(serverUser, masterReq.receiver,"",MessageType.ONLINEUSERS);
									mReply.users=ut;
									// mReply.receiver = masterReq.receiver;
									sendToMaster(mReply);
									break;

								case ONLINEUSERS:
									for (ClientThread ct : activeClients)
										if (ct != null)
											if(ct.cUser!=null)// didnt finisht registration yet 
											if (masterReq.receiver.userName.equals(ct.cUser.userName))
												ct.sendToClient(masterReq);
									System.out.println("Se3nding mebers LIst to client " + masterReq.sender.userName);
									break;
								case PRIVATE:
									sendPrivateMsg(masterReq);
									break;
								case PUBLIC:
									sendToAll(masterReq);
									break;
								case REGISTER:
									if(masterReq.msgBody.equals("true")) {
										register(masterReq.users[0],masterReq.receiver);
										
										for(ClientThread ct : activeClients) {
											if(ct!=null) {
												if(ct.cUser!=null)
											if(ct.cUser.userName.equals(masterReq.receiver.userName)){
												ct.sendToClient(masterReq);
												
												
											}else {
												ct.sendToClient( new Message(serverUser,masterReq.receiver,"",MessageType.NEWUSER));
											}
											}
										}
										
										
									}else {
										//the registration process is made with a temp user created with the client number
										//so we can communicate with the registering user without having a duplicated name we save the temp user in User[0] we manipulated this for less variables this isnt the intended use  
										for(ClientThread ct : activeClients) {
											if(ct!=null)
											if(ct.cUser.userName.equals(masterReq.users[0].userName)){
												ct.sendToClient(masterReq);
												
												
											}
										}
										
									}
									;break;
								case LOGOUT:sendToAll(masterReq);
								default:
									System.out.println("Default Master case " + masterReq);
									break;

								}

							}
					} catch (EOFException e) {

						continue;

					} catch (IOException | ClassNotFoundException e) {

						e.printStackTrace();

						// ResetMasterConnection();

					}
				}
			}
		};
		/*
		 * updateActiveUsers = new Thread() { public void run() { while(true) {
		 * 
		 * } }
		 * 
		 * };
		 */
		serverConnector.start();
		masterHandler.start();
	}

//----------------------sending methods-------------------------------------------------------------

	protected synchronized void register(User temp, User regUsr) {
		// TODO Auto-generated method stub
		for(int i=0;i<activeClients.length;i++) 
			if(activeClients[i]!=null)
				if(activeClients[i].cUser!=null)
			if(activeClients[i].cUser.userName.equals(temp.userName)) {
				activeClients[i].cUser=regUsr;
				break;
				
			}
		
		
	}

	private synchronized void sendToMaster(Message msg) {

		msg.decTTL();
		try {
			mO.writeObject(msg);
			mO.flush();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	private void sendPrivateMsg(Message masterReq) {

		synchronized (this) {

			for (ClientThread ct : activeClients) {
				if (ct != null)
					if (ct.cUser.userName.equals(masterReq.receiver.userName))
						ct.sendToClient(masterReq);
			}
		}

	}

	private void sendToAll(Message masterReq) {

		synchronized (this) {
			for (ClientThread ct : activeClients) {
				if (ct != null)
					if(ct.cUser!=null)//didnt finish registration yet 
					ct.sendToClient(masterReq);
			}
		}

	}

//----------------------------Helpers-------------------------------------------------------------------	
// 
 
	private User[] getActiveUsers() {
		System.out.println("Server getting active users");

		User[] users = new User[clientsNum + 1];
		int i = 0;
		for (ClientThread ct : activeClients) {
			if (ct != null) {
				users[i] = ct.cUser;
				i++;
			}
		}
		return users;
	}


	private ClientThread find(User receiver) {
		synchronized (this) {

			for (ClientThread ct : activeClients) {
				if (ct != null)
					if (ct.cUser.userName.equals(receiver.userName))
						return ct;
			}
		}
		return null;
	}
	// ----------------------------main-----------------------------------------------------------------

	public static void main(String[] args) {
		int sNum = 0;

		KeyPairGenerator keyGen;
		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair pair = keyGen.generateKeyPair();
			pbKey = pair.getPublic();
			prKey = pair.getPrivate();
			serverUser = new User("server-" + sNum, pbKey);
			Server s = new Server();
			sNum++;
			s.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//-------------------------------ClientThread-----------------------------------------------------------

	class ClientThread extends Thread {

		protected int clientNumber;
		protected Socket mySocket;

		protected ObjectOutputStream output;
		protected ObjectInputStream input;
		protected Boolean active = true;

		protected User cUser;

// -------------------------------constructor------------------------------------------------

		public ClientThread(int cNum, Socket myCS) {
			this.clientNumber = cNum;

			try {

				mySocket = myCS;
				output = new ObjectOutputStream(myCS.getOutputStream());
				input = new ObjectInputStream(myCS.getInputStream());

			} catch (IOException e) {
				e.printStackTrace();

				this.close();
			}
		}

		Thread clientHandler;

		public void run() {

			// recive("Welcome to X_O Secure chat room");

			clientHandler = new Thread() {
				Message receivedMsg;

				public void run() {
					while (active) {

						try {
							// TODO Auto-generated constructor stub
							receivedMsg = (Message) input.readObject();
							if (receivedMsg != null)
								if (receivedMsg.TTL > 0) {
									System.out.println("Server Received: \n" + receivedMsg);

									MessageType mt = receivedMsg.messageType;
									switch (mt) {
									case PUBLIC:
										sendToAll(receivedMsg);
										
										sendToMaster(receivedMsg);
										
										break;
									case PRIVATE:
										ClientThread pu = find(receivedMsg.receiver);
										if (pu == null) {
											sendToMaster(receivedMsg);

										} else {
											pu.sendToClient(receivedMsg);
										}
										break;
									case GETALLUSERS:

										Message m = new Message( serverUser,receivedMsg.sender,"",MessageType.GETALLUSERS);
										sendToMaster(m);
										break;

									case REGISTER:

	
										
										
										String cn = "";cn+=clientNumber;
										cUser= new User(cn,null);
										//cUser.userName=cn;
										receivedMsg.users = new User[1];
										receivedMsg.users[0]=cUser;
										receivedMsg.TTL=4;
										sendToMaster(receivedMsg);
										break;

										
									case LOGOUT:close();break;
										

									default:
										System.err.println("NO Such request");
										break;

									}

								}
						} catch (EOFException e) {

							// continue;
							System.out.println("end of connection");
							close();
							break;

						} catch (IOException | ClassNotFoundException e) {

							e.printStackTrace();
							
							close();

						}
					}
				}
			};

			clientHandler.start();
		}

//---------------------------------------helper ----------------------------------------------------------
		private void sendToClient(Message msg) {
			System.out.println("Sending to cliend \n " + msg.toString());
			Message m = new Message(msg);
			m.decTTL();

			try {
				output.writeObject(m);
				output.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public synchronized void close() {
			Message m =new Message(serverUser,null,cUser.userName,MessageType.LOGOUT);

			if (mySocket != null) {
				try {
					this.input.close();
					this.output.close();
					this.mySocket.close();

					
					 
						for (int i=0;i< activeClients.length;i++)
							if (activeClients[i] == this) {
								System.out.println("Client " + activeClients[i].cUser.userName + " log out");
								activeClients[i] = null;
								break;
							}

						clientsNum--;
						active = false;
					
					sendToMaster(m);
					sendToAll(m);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

}
