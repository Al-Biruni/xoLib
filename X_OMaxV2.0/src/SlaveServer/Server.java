package SlaveServer;

import Commons.*;


import java.io.IOException;

public class Server {
    int masterPort = 6500;
    int clientPort = 6000;


    protected final static int maxClients = 50;
    protected static Commons.SecretUser serverUser;
    MasterConnection masterConnection;
    ClientsConnections clientsConnections;


    public Server() {
        try {
            masterConnection = new SlaveServer.MasterConnection(this);
            clientsConnections = new SlaveServer.ClientsConnections(this);

            System.out.println(
                    "Connected to Master Server on port " + masterPort + "Waiting for clients on port " + clientPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void run() throws IOException {

        SlaveServer.ClientConnectionListener clientConnector = new ClientConnectionListener(clientPort, clientsConnections);
        MasterRequestHandler masterConnector = new MasterRequestHandler(masterConnection);

        clientConnector.start();
        masterConnector.start();
    }


    public static void main(String[] args) {

        try {
            serverUser = SecretUser.generateSecretUser("server-");
            Server s = new Server();
            s.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public User getUser() {
        return new User(serverUser.userName,serverUser.pk);
    }
}
