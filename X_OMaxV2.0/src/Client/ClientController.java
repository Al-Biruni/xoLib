package Client;

import Commons.Exceptions.MessageCouldnotBeEncryptedException;
import Commons.Message.Message;
import Commons.Message.MessageHandler;
import Commons.Message.MessageType;
import Commons.SecretUser;
import Commons.User;
import View.X_O;

public class ClientController implements ClientListener, MessageHandler {
    public X_O win;
    Client client ;
    public ClientController(Client client){

        this.client = client;
        client.setClientController(this);




    }



    @Override
    public void register(Message registrationResponse) {

        if (registrationResponse.msgBody.equals("true")) {

            win.userReg.setVisible(false);
            win.chatView();

            Message getMembers = new Message(client.user, client.server, "", MessageType.GETALLUSERS);
            client.sendMessage(getMembers);

        } else {
            win.usrNameText.setText("Choose diffrentuser name");

        }
    }
    @Override
    public void publicMessage(Message receivedPublicMsg) {
        win.msgArea.setText(win.msgArea.getText() + "\n " + receivedPublicMsg.sender.userName + " : "
                + receivedPublicMsg.msgBody);

    }

    @Override
    public void privateMessage(Message receivedMsg) {
        String dMsg = client.decrypt(receivedMsg);
        win.msgArea.setText(
                win.msgArea.getText() + "\n " + receivedMsg.sender.userName + " : " + dMsg);
    }

    @Override
    public void onlineUsersRequest(Message receivedMsg) {
        User[] onUsr = receivedMsg.users;
        for (User u : onUsr)
            if (u != null)
                win.listModel.addElement(u);
        // System.out.println(u.userName);
    }

    @Override
    public void sendToAll(Message message) {

    }

    @Override
    public void logout(Message receivedMsg) {
        for(int i=0;i<win.listModel.getSize();i++) {
            if(win.listModel.get(i).userName.equals(receivedMsg.msgBody)) {
                win.listModel.remove(i);
                break;
            }
        }
    }

    @Override
    public void getAllUsers(Message msg) {

    }

    @Override
    public void newUser(Message receivedMsg) {
        win.listModel.addElement(receivedMsg.receiver);
    }

    @Override
    public void onSend() throws MessageCouldnotBeEncryptedException {
        Message m = null;

        String msg = win.msgText.getText();

        if (win.rdbtnPrivate.isSelected()) {
            User receiver = win.listModel.get(win.list.getSelectedIndex());
            m = new Message(client.user, receiver, msg, MessageType.PRIVATE);
            //byte[] eMsg =
           client.encrypt(m);


            //m.enMsg = eMsg;
        } else {
            m = new Message(client.user, msg);
        }

        win.msgText.setText("");
       client.sendMessage(m);

        if (msg.equals("quit")) {
            win.setVisible(false);
            client.closeApp();

        }
    }

    @Override
    public void Register(String userName) {
        try {
            client.user = SecretUser.generateSecretUser(userName);
            Message m = new Message(client.user, null, "", MessageType.REGISTER);
            client.sendMessage(m);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setView(X_O win) {
        this.win=win;
    }
}
