package MasterServer;

import xoLib.*;
import xoLib.Exceptions.NotUniqueUserNameException;
import xoLib.Message.Message;
import xoLib.Message.MessageHandler;
import xoLib.Message.MessageType;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ServerThread extends Thread implements MessageHandler {
    protected int serverNumber;
    protected Socket mySocket;
    protected ObjectOutputStream mO;
    protected ObjectInputStream mI;

    protected ServerThread[] runningServers;
    MasterServer masterServer;

    ServerThread(MasterServer masterServer, Socket ss, int serverNumber) {
        this.mySocket = ss;
        this.serverNumber = serverNumber;
        this.masterServer = masterServer;
        runningServers = new ServerThread[masterServer.maxServers];

        try {
            this.mO = new ObjectOutputStream(mySocket.getOutputStream());
            this.mI = new ObjectInputStream(mySocket.getInputStream());

        } catch (Exception e) {
            System.err.println("Server connection error with master");
            e.printStackTrace();
            //closeThread();
        }

    }

    public void run() {

        Message m = null;

        while (true) {
            try {

                m = (Message) mI.readObject();
                if (m != null)
                    if (m.TTL > 0) {
                        System.out.println("Master reciving: " + m.toString());
                        Message.handel(m, this);
                        System.out.println(m.toString());

                    }


            } catch (IOException | ClassNotFoundException e) {
                //continue;
                closeThread();
                break;
            } catch (NotUniqueUserNameException e) {

            }

        }

    }


    private synchronized void logOut(String outUserName) {
        masterServer.clientsNum--;
        for (int i = 0; i < masterServer.knownActiveUsers.length; i++) {
            if (masterServer.knownActiveUsers[i] != null) {
                if (masterServer.knownActiveUsers[i].userName.equals(outUserName)) {
                    masterServer.knownActiveUsers[i] = null;
                }
            }
        }

    }

    private synchronized void sendToServers(Message msg) {
        System.out.println("sending to servers " + msg.toString());


        for (int i = 0; i < masterServer.serverThreads.length; i++)
            if (masterServer.serverThreads[i] != null)
                if (masterServer.serverThreads[i] != this)
                    masterServer.serverThreads[i].sendToServer(msg);


        System.out.println("DOne sending to servers");
    }

    private void sendToServer(Message msg) {
        try {
            Message m = new Message(msg);
            m.decTTL();
            mO.writeObject(m);
            mO.flush();

        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    private synchronized void closeThread() {

        try {
            mI.close();
            mO.close();


            for (int i = 0; i < runningServers.length; i++) {
                if (runningServers[i] == this) {
                    runningServers[i] = null;
                }
            }
        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    @Override
    public void register(Message registerMessage) {
        Boolean av = masterServer.checkUserName(registerMessage.sender.userName);
        if (av) {
            masterServer.knownActiveUsers[masterServer.clientsNum] = registerMessage.sender;
            Message r = new Message(masterServer.masterServerUser, registerMessage.sender, "true", MessageType.REGISTER);
            r.users = registerMessage.users;
            System.err.println(r.users[0].toString());
            masterServer.clientsNum++;
            sendToServer(r);
            sendToServers(r);
        } else {
            Message r = new Message(masterServer.masterServerUser, registerMessage.sender, "false", MessageType.REGISTER);
            r.users = registerMessage.users;
            sendToServer(r);
        }

    }

    @Override
    public void publicMessage(Message msg) {
        msg.TTL = 4;
        sendToServers(msg);

    }

    @Override
    public void privateMessage(Message msg) {
        msg.TTL = 4;
        sendToServers(msg);

    }

    @Override
    public void onlineUsersRequest(Message message) {

    }

    @Override
    public void sendToAll(Message message) {

    }

    @Override
    public void logout(Message msg) {
        logOut(msg.msgBody);
        sendToServers(msg);
    }

    @Override
    public void getAllUsers(Message msg) {

        Message rr = new Message(masterServer.masterServerUser, msg.receiver, "", MessageType.ONLINEUSERS);
        User[] onU = new User[masterServer.clientsNum];
        int i = 0;
        for (User u : masterServer.knownActiveUsers) {
            if (u != null) {
                onU[i] = u;
                i++;
            }
        }
        rr.users = onU;

        System.out.println("sending memberlist");

        sendToServer(rr);

    }

    @Override
    public void newUser(Message msg) {

    }
}
