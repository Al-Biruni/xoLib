package ClientJavaInterface.Model;


import ClientJavaInterface.ClientInterfaceController;
import xoLib.*;
import xoLib.Exceptions.NotUniqueUserNameException;
import xoLib.Message.Message;
import xoLib.Message.MessageType;


import java.io.EOFException;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public class Client {

    protected ObjectOutputStream oo;
    protected ObjectInputStream oi;

    protected Socket cS;
    protected String host;
    protected int serverPort;


    protected ClientInterfaceController clientController;

    protected User serverUser;


    protected SecretUser user;
    protected User[] knownActiveUsers;

    public Client() {
        serverPort = 6000;

    }

    public void connectToServer() {
        try {
            cS = new Socket("localhost", serverPort);
            oo = new ObjectOutputStream(cS.getOutputStream());
            oi = new ObjectInputStream(cS.getInputStream());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    public void run() {
        Message receivedMsg;
        while (true) {
            try {


                receivedMsg = (Message) oi.readObject();

                if (receivedMsg != null)
                    if (receivedMsg.TTL > 0) {
                        System.out.println("Client.Client recived : \n" + receivedMsg.toString());
                        Message.handel(receivedMsg, clientController);
                    }

            } catch (EOFException e) {
                closeApp();
                // continue;
                // System.out.println("endofM");
            } catch (IOException e) {

                closeApp();
            } catch (ClassNotFoundException e) {

                e.printStackTrace();
                closeApp();

            } catch (NotUniqueUserNameException e) {
                e.printStackTrace();
            }
        }


    }


    void sendMessage(Message m) {

        try {

            oo.writeObject(m);
            System.err.println(m.toString());
            oo.flush();
        } catch (IOException e) {

        }
    }

    void closeApp() {
        try {
            sendMessage(new Message(user, serverUser, user.userName, MessageType.LOGOUT));
            oo.close();
            oi.close();
            cS.close();
            System.exit(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }






    public void setClientController(ClientInterfaceController clientController) {
        this.clientController = clientController;
    }

    public String getUserName() {
        return this.user.userName;
    }

    public User getUser(){
        return  this.user;
    }
    public void setSecretUser(SecretUser secretUser){
    	this.user = secretUser;
	}
}
