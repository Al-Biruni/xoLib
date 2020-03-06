package masterServer;

import Commons.Message.Message;
import Commons.Message.MessageType;
import Commons.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class ServerThread extends Thread {
    protected int serverNumber;
    protected Socket mySocket;
    protected ObjectOutputStream mO;
    protected ObjectInputStream mI;

    protected ServerThread[] runningServers;
    MasterServer masterServer;
    ServerThread(MasterServer masterServer,Socket ss, int serverNumber) {
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
                System.out.println("Master reciving: " + m.toString());

            } catch (EOFException e) {
                //continue;
                closeThread();
                break;
            } catch (IOException | ClassNotFoundException e) {

                this.closeThread();
                break;

            }
            if (m != null)
                if (m.TTL > 0) {
                    MessageType reqType = m.messageType;
                    System.out.println(m.toString());
                    switch (reqType) {

                        case GETALLUSERS:

                            Message rr = new Message(masterServer.masterServerUser,m.receiver,"", MessageType.ONLINEUSERS);
                            User[] onU = new User[masterServer.clientsNum];
                            int i=0;
                            for(User u : masterServer.knownActiveUsers) {
                                if(u!=null) {
                                    onU[i]=u;
                                    i++;
                                }
                            }
                            rr.users=onU;

                            System.out.println("sending memberlist");

                            sendToServer(rr);

                            break;

                        case PUBLIC:
                        case PRIVATE:
                            m.TTL=4;
                            sendToServers(m);

                            break;
                        //case ONLINEUSERS:  now master have the main and only member list no more rerouting
                        //sendToServers(m);
                        //break;

                        case REGISTER:Boolean av = masterServer.checkUserName(m.sender.userName);
                            if(av) {
                                masterServer.knownActiveUsers[masterServer.clientsNum] = m.sender;
                                Message r = new Message(masterServer.masterServerUser,m.sender,"true",MessageType.REGISTER);
                                r.users=m.users;
                                System.err.println(r.users[0].toString());
                                masterServer.clientsNum++;
                                sendToServer(r);
                                sendToServers(r);
                            }else {
                                Message r = new Message(masterServer.masterServerUser,m.sender,"false",MessageType.REGISTER);
                                r.users=m.users;
                                sendToServer(r);
                            }break;

                        case LOGOUT: logOut(m.msgBody);sendToServers(m);


                        default:
                            break;
                    }
                }
        }

    }



    private synchronized void logOut(String outUserName) {
        masterServer.clientsNum--;
        for(int i=0;i<masterServer.knownActiveUsers.length;i++) {
            if(masterServer.knownActiveUsers[i]!=null) {
                if(masterServer.knownActiveUsers[i].userName.equals(outUserName)) {
                    masterServer.knownActiveUsers[i]=null;
                }
            }
        }

    }

    private synchronized  void sendToServers(Message msg) {
        System.out.println("sending to servers " + msg.toString());


        for (int i=0;i<masterServer.serverThreads.length;i++)
            if (masterServer.serverThreads[i] != null)
                if(masterServer.serverThreads[i]!=this)
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

}
