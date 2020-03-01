package SlaveServer;

import Commons.Message.Message;
import Commons.Message.MessageType;
import Commons.User;

import java.io.EOFException;
import java.io.IOException;

public class MasterRequestHandler extends Thread {
    MasterConnection masterConnection;

    public MasterRequestHandler(MasterConnection masterConnection) {
        this.masterConnection=masterConnection;

    }

    public void run() {
        Message masterReq;
        while (true) {

            try {// check for master
                masterReq = (Message) masterConnection.mI.readObject();
                if (masterReq != null)
                    if (masterReq.TTL > 0) {

                        System.out.println("MAster  request: " + masterReq.toString());

                        switch (masterReq.messageType) {


                            case ONLINEUSERS: onlineUsersRequest(masterReq);

                                break;
                            case PRIVATE:
                                masterConnection.sendPrivateMsg(masterReq);
                                break;
                            case PUBLIC:
                                masterConnection.sendToAll(masterReq);
                                break;
                            case REGISTER: registerRequest(masterReq);
                                break;
                            case LOGOUT:
                                masterConnection.sendToAll(masterReq);
                            default:
                                System.out.println("Default Master case " + masterReq);
                                break;

                        }

                    }
            } catch (EOFException e) {

                continue;

            } catch (IOException | ClassNotFoundException e) {

                e.printStackTrace();

                // ResetMasterConnection();

            }
        }
    }

    private void registerRequest(Message masterReq) {
        if (masterReq.msgBody.equals("true")) {
            masterConnection.register(masterReq.users[0], masterReq.receiver);

            for (ClientThread ct : masterConnection.getActiveClients()) {
                    if (ct.cUser != null)
                        if (ct.cUser.userName.equals(masterReq.receiver.userName)) {
                            ct.sendToClient(masterReq);


                        } else {
                            ct.sendToClient(new Message(masterConnection.getServerUser(), masterReq.receiver, "", MessageType.NEWUSER));
                        }

            }


        } else {
            //the registration process is made with a temp user created with the client number
            //so we can communicate with the registering user without having a duplicated name
            // we save the temp user in User[0] we manipulated this for less variables this isnt the intended use
            for (ClientThread ct : masterConnection.getActiveClients()) {
                    if (ct.cUser.userName.equals(masterReq.users[0].userName)) {
                        ct.sendToClient(masterReq);


                    }
            }

        }
    }

    private void onlineUsersRequest(Message masterReq) {
        for (ClientThread ct :masterConnection.getActiveClients())
                if (ct.cUser != null)// didn't finish registration yet
                    if (masterReq.receiver.userName.equals(ct.cUser.userName))
                        ct.sendToClient(masterReq);
        System.out.println("Se3nding mebers LIst to client " + masterReq.sender.userName);
    }

}
