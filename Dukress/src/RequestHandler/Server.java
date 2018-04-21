



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Server {

	protected int serverNum, clientsNum;
	protected Socket newClientS;
	protected ServerSocket mySocket;
	protected final static int maxClients = 50;
	protected ClientThread[] activeClients = new ClientThread[maxClients];


	public Server(int num) {
		this.serverNum = num;
		clientsNum = 0;

		int cPort = 6010;

		try {
			mySocket = new ServerSocket(cPort);
			newClientS = new Socket();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}

	public void run() throws IOException {

		while (true) {

			newClientS = mySocket.accept();
			if (newClientS != null) {

				System.out.println("COnnecting creating new THread");
				activeClients[clientsNum] = new ClientThread(clientsNum,
						newClientS, activeClients);
				activeClients[clientsNum].start();
				clientsNum++;
				newClientS = new Socket();

			}

		}

	}

	public static void main(String[] args) {
		int sNum = 0;
		Server s = new Server(sNum);
		sNum++;
		try {
			s.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class ClientThread extends Thread {

		protected String clientName, myMsg;
		protected int clientNumber;
		protected Socket mySocket, myObjSocket;
		protected ObjectInputStream input;
		protected ObjectOutputStream output;
		protected ClientThread[] active = new ClientThread[maxClients];

		public ClientThread(int cNum, Socket myCS, ClientThread[] active) {
			this.clientNumber = cNum;
			System.out.println("Creating client number  " + cNum);
			this.active = active;
			try {

				mySocket = myCS;
				output = new ObjectOutputStream(myCS.getOutputStream());
				input = new ObjectInputStream(myCS.getInputStream());
				

			} catch (IOException e) {
				e.printStackTrace();

				try {
					this.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		public void run() {

			Thread handler = new Thread() {
				String[] msgSplit;

				public void run() {
					try {
while(true){
	System.out.println("running");
	Request r = (Request) input.readObject();
						myMsg = r.toString();
						
						msgSplit = myMsg.split(",");
						if (msgSplit.length > 0)
							switch (msgSplit[0].split(" ")[0]) {
							case "GET":
								
								
										
									Response t = new Response("200 ok ",msgSplit[2],msgSplit[3] );
									
									 File file = new File(msgSplit[0].split(" ")[1]);

									 byte[] b = new byte[(int) file.length()];
							        
									t.setData( b);
							output.writeObject(t);
							output.flush();
								System.out.println(myMsg);
						
								break;

							default:
								System.out.println(myMsg);
								break;

							}
						
}

					} catch (IOException e) {
						Response t = new Response("404 eror ",msgSplit[2],msgSplit[3] );
						try {
							output.writeObject(t);
							output.flush();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						System.out.println("no suchfile");
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};

			handler.start();

		}



		public void close() throws IOException {
			if (mySocket != null) {
				input.close();
				output.close();
				mySocket.close();
			}
		}

	}

}
