package SlaveServer;

import Commons.Message.Message;

import java.io.IOException;
import java.io.ObjectInputStream;


public class MasterRequestListener extends Thread {
    ObjectInputStream masterInputStream;
    SlaveServerMasterMessageHandler  masterRequestHandler;

    public MasterRequestListener(Server server) {
        masterRequestHandler = server.getMasterRequestHandler();
        this.masterInputStream = server.masterConnectionManger.getInputStream();

    }

    public void run() {
        Message masterReq;
        while (true) {
            // check for master
            try {
                masterReq = (Message) masterInputStream.readObject();

                if (validMasterRequest(masterReq)) {
                    Message.handel(masterReq,masterRequestHandler);
                    System.out.println("MAster  request: " + masterReq.toString());

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    private boolean validMasterRequest(Message masterReq) {
        if(masterReq == null || masterReq.TTL<=0)
            return false;
        return true;
    }


}
