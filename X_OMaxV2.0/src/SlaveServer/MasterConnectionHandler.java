package SlaveServer;

import Commons.Message.Message;
import Commons.Message.MessageType;
import Commons.User;

import java.io.EOFException;
import java.io.IOException;

public class MasterHandlerThread extends Thread {
    Server server;

    public MasterHandlerThread(Server server) {
        this.server = server;

    }

    public void run() {
        Message masterReq;
        while (true) {

            try {// check for master
                masterReq = (Message) server.mI.readObject();
                if (masterReq != null)
                    if (masterReq.TTL > 0) {

                        System.out.println("MAster SlaveServer.Server request: " + masterReq.toString());

                        switch (masterReq.messageType) {

                            case GETALLUSERS:
                                User[] ut = server.getActiveUsers();
                                Message mReply = new Message(server.serverUser, masterReq.receiver, "", MessageType.ONLINEUSERS);
                                mReply.users = ut;
                                // mReply.receiver = masterReq.receiver;
                                server.sendToMaster(mReply);
                                break;

                            case ONLINEUSERS:
                                for (ClientThread ct : server.activeClients)
                                    if (ct != null)
                                        if (ct.cUser != null)// didn't finish registration yet
                                            if (masterReq.receiver.userName.equals(ct.cUser.userName))
                                                ct.sendToClient(masterReq);
                                System.out.println("Se3nding mebers LIst to client " + masterReq.sender.userName);
                                break;
                            case PRIVATE:
                                server.sendPrivateMsg(masterReq);
                                break;
                            case PUBLIC:
                                server.sendToAll(masterReq);
                                break;
                            case REGISTER:
                                if (masterReq.msgBody.equals("true")) {
                                    server.register(masterReq.users[0], masterReq.receiver);

                                    for (ClientThread ct : server.activeClients) {
                                        if (ct != null) {
                                            if (ct.cUser != null)
                                                if (ct.cUser.userName.equals(masterReq.receiver.userName)) {
                                                    ct.sendToClient(masterReq);


                                                } else {
                                                    ct.sendToClient(new Message(server.serverUser, masterReq.receiver, "", MessageType.NEWUSER));
                                                }
                                        }
                                    }


                                } else {
                                    //the registration process is made with a temp user created with the client number
                                    //so we can communicate with the registering user without having a duplicated name
                                    // we save the temp user in User[0] we manipulated this for less variables this isnt the intended use
                                    for (ClientThread ct : server.activeClients) {
                                        if (ct != null)
                                            if (ct.cUser.userName.equals(masterReq.users[0].userName)) {
                                                ct.sendToClient(masterReq);


                                            }
                                    }

                                }
                                ;
                                break;
                            case LOGOUT:
                                server.sendToAll(masterReq);
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

}
