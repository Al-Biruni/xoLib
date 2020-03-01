package SlaveServer;

import Commons.Message.Message;
import Commons.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MasterConnection {
    protected Socket masterS;

    protected ObjectOutputStream mO;
    protected ObjectInputStream mI;
    protected Server server;

    public MasterConnection(Server server) throws IOException {
        this.server = server;
        masterS = new Socket("localhost", server.masterPort);
        mO = new ObjectOutputStream(masterS.getOutputStream());
        mI = new ObjectInputStream(masterS.getInputStream());
    }

    synchronized void sendToMaster(Message msg) {

        msg.decTTL();
        try {
            mO.writeObject(msg);
            mO.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void sendPrivateMsg(Message masterReq) {
        server.clientsConnections.sendPrivateMsg(masterReq);
    }

    public void sendToAll(Message masterReq) {
        server.clientsConnections.sendToAll(masterReq);
    }

    public void register(User user, User receiver) {
        server.clientsConnections.register(user,receiver);
    }

    public ClientThread[] getActiveClients() {
        return  server.clientsConnections.getActiveClients();
    }

    public User getServerUser() {
        return server.getUser();
    }
}
