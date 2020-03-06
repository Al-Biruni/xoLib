package SlaveServer;

import Commons.Message.Message;
import Commons.User;


import java.net.Socket;

public class ClientsConnections {
    protected Server server;
    protected int clientsNum = 0;
    protected ClientThread[] maxThreadPool;
    protected ClientThread[] activeClients;
    boolean dirty = true;

    public ClientsConnections(Server server) {
        this.server = server;
        maxThreadPool = new ClientThread[server.maxClients];
    }

    synchronized void addNewClient(Socket newClientSocket) {
        dirty = true;

        for (int i = 0; i < maxThreadPool.length; i++)
            if (maxThreadPool[i] == null) {
                maxThreadPool[i] = new ClientThread(this, clientsNum, newClientSocket);
                maxThreadPool[i].start();
                clientsNum++;
                break;
            }

    }

    synchronized void sendPrivateMsg(Message msg) {
        for (ClientThread ct : maxThreadPool) {
            if (ct != null)
                if (ct.cUser.userName.equals(msg.receiver.userName))
                    ct.sendToClient(msg);
        }
    }

    synchronized void sendToAll(Message msg) {
        for (ClientThread ct : maxThreadPool) {
            if (ct != null)
                if (ct.cUser != null)//didnt finish registration yet
                    ct.sendToClient(msg);
        }


    }

    synchronized void register(User temp, User regUsr) {
        for (int i = 0; i < maxThreadPool.length; i++)
            if (maxThreadPool[i] != null)
                if (maxThreadPool[i].cUser != null)
                    if (maxThreadPool[i].cUser.userName.equals(temp.userName)) {
                        maxThreadPool[i].cUser = regUsr;
                        break;

                    }


    }

    synchronized ClientThread find(User receiver) {

        for (ClientThread ct : getActiveClients()) {
                if (ct.cUser.userName.equals(receiver.userName))
                    return ct;
        }

        return null;
    }

    synchronized void sendToMaster(Message msg) {
        server.masterConnection.sendToMaster(msg);
    }

    synchronized ClientThread[] getActiveClients() {
        if(!dirty)
            return activeClients;

        ClientThread[] cts = new ClientThread[clientsNum];
        int i = 0, c = 0;
        while (i < clientsNum) {
            if (maxThreadPool[c] != null) {
                cts[i] = maxThreadPool[c];
                i++;
            }
            c++;
        }
        dirty = false;
        activeClients = cts;
        return cts;

    }

    public User getServerUser() {
        return  server.getUser();
    }

    public void close(ClientThread clientThread) {
        dirty=true;

        for (int i = 0; i< maxThreadPool.length; i++)
            if (maxThreadPool[i] == clientThread) {
                System.out.println("Client " + maxThreadPool[i].cUser.userName + " log out");
                maxThreadPool[i] = null;
                clientsNum--;
                break;
            }
    }
}
