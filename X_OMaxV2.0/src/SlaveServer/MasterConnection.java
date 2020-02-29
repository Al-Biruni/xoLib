package SlaveServer;

import Commons.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MasterConnection {
    protected Socket masterS;

    protected ObjectOutputStream mO;
    protected ObjectInputStream mI;

    public MasterConnection(int masterPort) throws IOException {

        masterS = new Socket("localhost", masterPort);
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
}
