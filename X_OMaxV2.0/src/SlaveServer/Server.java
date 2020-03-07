package SlaveServer;

import Commons.*;


import java.io.IOException;

public class Server {

    protected final static int maxClients = 50;
    protected final static String masterIP = "localhost";
    final static int masterPort = 6500;
    final static int clientPort = 6000;

    protected SecretUser serverUser;
     SlaveServerMasterMessageHandler masterRequestHandler;
    MasterConnectionManger masterConnectionManger;
    ClientsConnectionsManager clientsConnectionsManager;


    public Server(SecretUser serverUser) {
        try {
            this.serverUser = serverUser;

            masterConnectionManger = new MasterConnectionManger(masterIP, masterPort);

            clientsConnectionsManager = new ClientsConnectionsManager(this);

            masterRequestHandler = new SlaveServerMasterMessageHandler(serverUser,
                                                                        clientsConnectionsManager,masterConnectionManger);

            System.out.println(
                    "Connected to Master Server on port " + masterPort + "Waiting for clients on port " + clientPort);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void run() throws IOException {

        ClientConnectionListener clientConnectionListener = new ClientConnectionListener(clientPort, clientsConnectionsManager);
        MasterRequestListener masterRequestListener = new MasterRequestListener(this);

        clientConnectionListener.start();
        masterRequestListener.start();
    }


    public SlaveServerMasterMessageHandler getMasterRequestHandler() {
        return masterRequestHandler;
    }


    public User getUser() {
        return serverUser;

    }


    public static void main(String[] args) {

        try {
            SecretUser serverUser = SecretUser.generateSecretUser("server-0");
            Server s = new Server(serverUser);
            s.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
