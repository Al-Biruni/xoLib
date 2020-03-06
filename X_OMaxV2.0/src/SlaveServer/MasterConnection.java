package SlaveServer;

import Commons.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MasterConnection {
    protected Socket masterSocket;
    protected ObjectOutputStream masterOutStream;
    protected ObjectInputStream masterInStream;


    public MasterConnection(String masterIP , int masterPort) throws IOException {
        masterSocket = new Socket(masterIP,masterPort);
        masterOutStream = new ObjectOutputStream(this.masterSocket.getOutputStream());
        masterInStream = new ObjectInputStream(this.masterSocket.getInputStream());
    }

    synchronized void sendToMaster(Message msg) {

        msg.decTTL();
        try {
            masterOutStream.writeObject(msg);
            masterOutStream.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    public ObjectInputStream getInputStream() {
        return masterInStream;
    }
}
