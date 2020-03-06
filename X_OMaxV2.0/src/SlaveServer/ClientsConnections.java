package SlaveServer;

import Commons.Message.Message;
import Commons.User;


import java.net.Socket;

public class ClientsConnections {
    protected Server server;
    protected int clientsNum = 0;
    protected ClientThread[] activeClients;

    public ClientsConnections(Server server) {
        this.server = server;
        activeClients = new ClientThread[server.maxClients];
    }

    synchronized void addNewClient(Socket newClientSocket) {

        for (int i = 0; i < activeClients.length; i++)
            if (activeClients[i] == null) {
                activeClients[i] = new ClientThread(this, clientsNum, newClientSocket);
                activeClients[i].start();
                clientsNum++;
                break;
            }

    }

    synchronized void sendPrivateMsg(Message msg) {
        for (ClientThread ct : activeClients) {
            if (ct != null)
                if (ct.cUser.userName.equals(msg.receiver.userName))
                    ct.sendToClient(msg);
        }
    }

    synchronized void sendToAll(Message msg) {
        for (ClientThread ct : activeClients) {
            if (ct != null)
                if (ct.cUser != null)//didnt finish registration yet
                    ct.sendToClient(msg);
        }


    }

    synchronized void register(User temp, User regUsr) {
        for (int i = 0; i < activeClients.length; i++)
            if (activeClients[i] != null)
                if (activeClients[i].cUser != null)
                    if (activeClients[i].cUser.userName.equals(temp.userName)) {
                        activeClients[i].cUser = regUsr;
                        break;

                    }


    }

    synchronized ClientThread find(User receiver) {

        for (ClientThread ct : activeClients) {
            if (ct != null)
                if (ct.cUser.userName.equals(receiver.userName))
                    return ct;
        }

        return null;
    }

    synchronized void sendToMaster(Message msg) {
        server.masterConnection.sendToMaster(msg);
    }

    synchronized ClientThread[] getActiveClients() {
        ClientThread[] cts = new ClientThread[clientsNum];
        int i = 0, c = 0;
        while (i < clientsNum) {
            if (activeClients[c] != null) {
                cts[i] = activeClients[c];
                i++;
            }
            c++;
        }
        return cts;

    }

    public User getServerUser() {
        return  server.getUser();
    }

    public void close(ClientThread clientThread) {

        for (int i=0;i< activeClients.length;i++)
            if (activeClients[i] == clientThread) {
                System.out.println("Client " + activeClients[i].cUser.userName + " log out");
                activeClients[i] = null;
                clientsNum--;
                break;
            }
    }
}
