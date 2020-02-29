package SlaveServer;//-------------------------------SlaveServer.ClientThread-----------------------------------------------------------

import Commons.Message.Message;
import Commons.Message.MessageType;
import Commons.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ClientThread extends Thread {

    protected int clientNumber;
    protected Socket mySocket;

    protected ObjectOutputStream output;
    protected ObjectInputStream input;
    protected Boolean active = true;

    protected User cUser;
// -------------------------------constructor------------------------------------------------

    public ClientThread(int cNum, Socket myCS) {
        this.clientNumber = cNum;


        try {

            mySocket = myCS;
            output = new ObjectOutputStream(myCS.getOutputStream());
            input = new ObjectInputStream(myCS.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();

            this.close();
        }
    }

    Thread clientHandler;

    public void run() {

        // recive("Welcome to View.X_O Secure chat room");

        clientHandler = new Thread() {
            Message receivedMsg;

            public void run() {
                while (active) {

                    try {
                        // TODO Auto-generated constructor stub
                        receivedMsg = (Message) input.readObject();
                        if (receivedMsg != null)
                            if (receivedMsg.TTL > 0) {
                                System.out.println("SlaveServer.Server Received: \n" + receivedMsg);

                                MessageType mt = receivedMsg.messageType;
                                switch (mt) {
                                    case PUBLIC:
                                        server.sendToAll(receivedMsg);

                                        server.masterConnection.sendToMaster(receivedMsg);

                                        break;
                                    case PRIVATE:
                                        ClientThread pu = server.find(receivedMsg.receiver);
                                        if (pu == null) {
                                            server.masterConnection.sendToMaster(receivedMsg);

                                        } else {
                                            pu.sendToClient(receivedMsg);
                                        }
                                        break;
                                    case GETALLUSERS:

                                        Message m = new Message( server.serverUser,receivedMsg.sender,"", MessageType.GETALLUSERS);
                                        server.masterConnection.sendToMaster(m);
                                        break;

                                    case REGISTER:




                                        String cn = "";cn+=clientNumber;
                                        cUser= new User(cn,null);
                                        //cUser.userName=cn;
                                        receivedMsg.users = new User[1];
                                        receivedMsg.users[0]=cUser;
                                        receivedMsg.TTL=4;
                                        server.masterConnection.sendToMaster(receivedMsg);
                                        break;


                                    case LOGOUT:close();break;


                                    default:
                                        System.err.println("NO Such request");
                                        break;

                                }

                            }
                    } catch (EOFException e) {

                        // continue;
                        System.out.println("end of connection");
                        close();
                        break;

                    } catch (IOException | ClassNotFoundException e) {

                        e.printStackTrace();

                        close();

                    }
                }
            }
        };

        clientHandler.start();
    }

    //---------------------------------------helper ----------------------------------------------------------
    void sendToClient(Message msg) {
        System.out.println("Sending to cliend \n " + msg.toString());
        Message m = new Message(msg);
        m.decTTL();

        try {
            output.writeObject(m);
            output.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public synchronized void close() {
        Message m =new Message(server.serverUser,null,cUser.userName,MessageType.LOGOUT);

        if (mySocket != null) {
            try {
                this.input.close();
                this.output.close();
                this.mySocket.close();



                for (int i=0;i< server.activeClients.length;i++)
                    if (server.activeClients[i] == this) {
                        System.out.println("Client " + server.activeClients[i].cUser.userName + " log out");
                        server.activeClients[i] = null;
                        break;
                    }

                server.clientsNum--;
                active = false;

                server.masterConnection.sendToMaster(m);
                server.sendToAll(m);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

}