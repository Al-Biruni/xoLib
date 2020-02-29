package SlaveServer;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionHandler extends Thread {

    Server server;
    public ClientConnectionHandler(Server server){
        this.server=server;
    }


        public void run() {
            while (true) {

                try {
                    server.newClientS = server.mySocket.accept();
                } catch (IOException e) {
                    System.err.println("Client.Client Socket Error while accepting ");
                    e.printStackTrace();
                }
                if (server.newClientS != null) {

                    System.out.println("COnnecting creating new THread");
                    server.activeClients[server.clientsNum] = new ClientThread(server,server.clientsNum, server.newClientS);
                    server.activeClients[server.clientsNum].start();
                    server.clientsNum++;
                    server.newClientS = new Socket();

                }

            }

        }

}
