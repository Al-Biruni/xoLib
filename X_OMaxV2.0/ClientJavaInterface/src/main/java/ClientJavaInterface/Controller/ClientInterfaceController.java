package ClientJavaInterface.Controller;

import ClientJavaInterface.Model.Client;
import ClientJavaInterface.View.ClientInterfaceListener;
import xoLib.*;
import ClientJavaInterface.View.X_O;
import xoLib.Exceptions.MessageCouldnotBeEncryptedException;
import xoLib.Message.Message;

import xoLib.Message.MessageType;


public class ClientInterfaceController  extends ClientMessageHandler implements ClientInterfaceListener {

    public ClientInterfaceController(Client client){
        super(client);
        client.setClientController(this);


    }




    @Override
    public void onSend() throws MessageCouldnotBeEncryptedException {
        Message m = null;

        String msg = win.msgText.getText();

        if (win.rdbtnPrivate.isSelected()) {
            User receiver = win.listModel.get(win.list.getSelectedIndex());
            m = new Message(client.getUser(), receiver, msg, MessageType.PRIVATE);
           client.getUser().encrypt(m);

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
    public void onRegisterBtnClick(String userName) {
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
