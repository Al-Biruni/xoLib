package SlaveServer;

import Commons.*;
import Commons.Message.Message;


import java.io.IOException;

public class Server {
    int masterPort = 6500;
    int clientPort = 6000;


    protected final static int maxClients = 50;
    protected static SecretUser serverUser;
    MasterConnection masterConnection;





    // ------------------------constructor and main------------------------------
    public Server() {

        initSockets();
    }

    private void initSockets() {
        try {
            masterConnection = new MasterConnection(masterPort);


            System.out.println(
                    "Connected to Master Server on port " + masterPort + "Waiting for clients on port " + clientPort);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void run() {

        ClientConnectionThread clientConnector = new ClientConnectionThread(clientPort);
        MasterConnectionHandler masterConnector = new MasterConnectionHandler(this);

        clientConnector.start();
        masterConnector.start();
    }

//----------------------sending methods-------------------------------------------------------------

    protected synchronized void register(User temp, User regUsr) {
        // TODO Auto-generated method stub
        for (int i = 0; i < activeClients.length; i++)
            if (activeClients[i] != null)
                if (activeClients[i].cUser != null)
                    if (activeClients[i].cUser.userName.equals(temp.userName)) {
                        activeClients[i].cUser = regUsr;
                        break;

                    }


    }




    void sendPrivateMsg(Message masterReq) {

        synchronized (this) {

            for (ClientThread ct : activeClients) {
                if (ct != null)
                    if (ct.cUser.userName.equals(masterReq.receiver.userName))
                        ct.sendToClient(masterReq);
            }
        }

    }

    void sendToAll(Message masterReq) {

        synchronized (this) {
            for (ClientThread ct : activeClients) {
                if (ct != null)
                    if (ct.cUser != null)//didnt finish registration yet
                        ct.sendToClient(masterReq);
            }
        }

    }

//----------------------------Helpers-------------------------------------------------------------------	
// 

    User[] getActiveUsers() {
        System.out.println("SlaveServer.Server getting active users");

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


    synchronized ClientThread find(User receiver) {

        for (ClientThread ct : activeClients) {
            if (ct != null)
                if (ct.cUser.userName.equals(receiver.userName))
                    return ct;
        }

        return null;
    }
    // ----------------------------main-----------------------------------------------------------------

    public static void main(String[] args) {

        try {
            serverUser =  SecretUser.generateSecretUser("server-");
            Server s = new Server();
            s.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
