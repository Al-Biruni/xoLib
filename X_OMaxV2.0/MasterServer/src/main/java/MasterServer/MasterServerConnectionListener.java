package MasterServer;

import java.io.IOException;
import java.net.Socket;

public class MasterServerConnectionListener extends Thread {
    MasterServer masterServer;

public MasterServerConnectionListener(MasterServer masterServer){
    this.masterServer = masterServer;

}

@Override
    public void run() {


            while (true) {

                try {
                    System.out.println("waiting for more servers");
                    masterServer.ss = MasterServer.Mserver.accept();
                } catch (IOException e) {

                    e.printStackTrace();
                }

                if (masterServer.ss != null) {
                    System.out.println("SS not null");
                    for (int serverNumber = 0; serverNumber < masterServer.maxServers; serverNumber++) {
                        if (masterServer.serverThreads[serverNumber] == null) {
                            System.out.println("SlaveServer.Server conected......." + serverNumber);
                            masterServer.serverThreads[serverNumber] = new ServerThread(masterServer,masterServer.ss, serverNumber);
                            System.out.println("Starting serverThread");
                            masterServer. serverThreads[serverNumber].start();
                            System.out.println("Done");
                            masterServer.ss = new Socket();
                            break;
                        } else if (serverNumber == masterServer.maxServers) {
                            try {
                                System.out.println("MaxServers reached");
                                masterServer.ss.close();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                        }
                    }

                }

            }
        }



}
