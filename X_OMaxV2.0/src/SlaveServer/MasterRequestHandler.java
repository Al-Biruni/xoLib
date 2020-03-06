package SlaveServer;

import Commons.Message.Message;
import Commons.Message.MessageHandler;


public class MasterRequestHandler extends Thread {
    MessageHandler masterMsgHandler;

    public MasterRequestHandler(MessageHandler messageHandler) {
        this.masterMsgHandler = messageHandler;

    }

    public void run() {
        Message masterReq;
        while (true) {
            // check for master
            masterReq = (Message) masterConnection.mI.readObject();
            if (validMasterRequest(masterReq)) {
                masterMsgHandler.handelMessage(masterReq);
                System.out.println("MAster  request: " + masterReq.toString());

            }
        }
    }

    private boolean validMasterRequest(Message masterReq) {
        if(masterReq == null || masterReq.TTL<=0)
            return false;
        return true;
    }


}
