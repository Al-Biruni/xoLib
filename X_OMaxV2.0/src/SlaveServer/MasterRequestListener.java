package SlaveServer;

import Commons.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;


public class MasterRequestListener extends Thread {
    ObjectInputStream masterInputStream;

    public MasterRequestListener(ObjectInputStream masterInputStream) {
        this.masterInputStream = masterInputStream;

    }

    public void run() {
        Message masterReq;
        while (true) {
            // check for master
            try {
                masterReq = (Message) masterInputStream.readObject();
                if (validMasterRequest(masterReq)) {
                   handelMessage(masterReq);
                    System.out.println("MAster  request: " + masterReq.toString());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private void handelMessage(Message masterReq) {

    }

    private boolean validMasterRequest(Message masterReq) {
        if(masterReq == null || masterReq.TTL<=0)
            return false;
        return true;
    }


}
