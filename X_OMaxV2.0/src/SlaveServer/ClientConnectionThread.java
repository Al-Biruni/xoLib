package SlaveServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientConnectionThread extends Thread {

    protected Socket newClientS;
    protected ServerSocket mySocket;
    protected ClientThread[] activeClients ;
    protected int clientsNum=0;


    public ClientConnectionThread(int clientPort, int maxClients) throws IOException {
        activeClients = new ClientThread[maxClients];
        mySocket = new ServerSocket(clientPort);
        newClientS = new Socket();
    }


        public void run() {
            while (true) {

                try {
                    newClientS = mySocket.accept();
                } catch (IOException e) {
                    System.err.println("Client.Client Socket Error while accepting ");
                    e.printStackTrace();
                }
                if (newClientS != null) {

                    System.out.println("COnnecting creating new THread");
                    activeClients[clientsNum] = new ClientThread(server,clientsNum, server.newClientS);
                    activeClients[clientsNum].start();
                    clientsNum++;
                    newClientS = new Socket();

                }

            }

        }

}
