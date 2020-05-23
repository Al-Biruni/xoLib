package ClientJavaInterface.Controller;

import ClientJavaInterface.Model.Client;
import ClientJavaInterface.View.X_O;
import xoLib.Exceptions.NotUniqueUserNameException;
import xoLib.Message.Message;
import xoLib.Message.MessageHandler;
import xoLib.Message.MessageType;
import xoLib.User;

public class ClientMessageHandler implements MessageHandler {
    public X_O win;
    Client client;


    public ClientMessageHandler(Client client) {
        this.client = client;
    }

    @Override
    public void register(Message registrationResponse) throws NotUniqueUserNameException {

        if (registrationResponse.msgBody.equals("true")) {

            win.userReg.setVisible(false);
            win.chatView();

            Message getMembers = new Message(client.user, client.serverUser, "", MessageType.GETALLUSERS);
            client.sendMessage(getMembers);

        } else {
            win.usrNameText.setText("Choose diffrent user name");
            throw new NotUniqueUserNameException(new Exception("Not unique user Name "));

        }
    }

    @Override
    public void publicMessage(Message receivedPublicMsg) {
        win.msgArea.setText(win.msgArea.getText() + "\n " + receivedPublicMsg.sender.userName + " : "
                + receivedPublicMsg.msgBody);

    }

    @Override
    public void privateMessage(Message receivedMsg) {
        String dMsg = client.user.decrypt(receivedMsg);
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
        for (int i = 0; i < win.listModel.getSize(); i++) {
            if (win.listModel.get(i).userName.equals(receivedMsg.msgBody)) {
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

}
