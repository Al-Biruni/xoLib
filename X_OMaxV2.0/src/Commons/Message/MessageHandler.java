package Commons.Message;

import SlaveServer.Server;


import java.io.EOFException;
import java.io.IOException;

public class MessageHandler implements ServerMessageHandler{

    public MessageHandler(Server server){

    }



    public void handelMessage(Message masterReq) {
        try{

            switch (masterReq.messageType) {


                case ONLINEUSERS:
                    onlineUsersRequest(masterReq);

                    break;
                case PRIVATE:
                    sendPrivateMessage(masterReq);
                    break;
                case PUBLIC:
                    sendToAll(masterReq);
                    break;
                case REGISTER:
                    registerRequest(masterReq);
                    break;
                case LOGOUT:
                    sendToAll(masterReq);
                default:
                    System.out.println("Default Master case " + masterReq);
                    break;

            }

        } catch (ClassNotFoundException e) {

        e.printStackTrace();

        // ResetMasterConnection();

    }
        }




    private void registerRequest(Message masterReq) {
        if (masterReq.msgBody.equals("true")) {
            uniqueName(masterReq);

        } else {
            notUniqueName(masterReq);
        }
    }


    private void uniqueName(Message masterReq) {
        masterConnection.register(masterReq.users[0], masterReq.receiver);

        for (ClientThread ct : masterConnection.getActiveClients()) {
            if (ct.cUser.userName.equals(masterReq.receiver.userName)) {
                ct.sendToClient(masterReq);

            } else {
                ct.sendToClient(new Message(masterConnection.getServerUser(), masterReq.receiver, "", MessageType.NEWUSER));
            }

        }
    }

    private void notUniqueName(Message masterReq) {
        //the registration process is made with a temp user created with the client number
        //so we can communicate with the registering user without having a duplicated name
        // we save the temp user in User[0] we manipulated this for less variables this isnt the intended use
        for (ClientThread ct : masterConnection.getActiveClients()) {
            if (ct.cUser.userName.equals(masterReq.users[0].userName)) {
                ct.sendToClient(masterReq);


            }
        }
    }

    private void onlineUsersRequest(Message masterReq) {
        for (ClientThread ct : masterConnection.getActiveClients())
            if (ct.cUser != null)// didn't finish registration yet
                if (masterReq.receiver.userName.equals(ct.cUser.userName))
                    ct.sendToClient(masterReq);
        System.out.println("Se3nding members LIst to client " + masterReq.sender.userName);
    }

    @Override
    public void sendToAll(Message masterReq) {

    }

    @Override
    public void sendPrivateMessage(Message masterReq) {

    }

    @Override
    public void RegisterUser() {

    }
}
