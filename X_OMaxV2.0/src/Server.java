import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server  {
	
	  protected  int serverNum, clientsNum;
	  protected  Socket newClientS,masterS,objS;
	  protected   ServerSocket mySocket;
	  protected final static int maxClients= 50;
	  protected  ClientThread[] activeClients = new ClientThread[maxClients];
		protected DataInputStream mI;
		protected DataOutputStream mO;
		
	  
	  public Server(int num){
		this.serverNum =num;
		clientsNum=0;
		int serverPort = 6500;
		int cPort = 6000;
		
		try {
			mySocket = new ServerSocket(cPort);
			newClientS = new Socket();
			masterS = new Socket("localhost",serverPort);
			System.out.println("Connected to Master Server on port " + serverPort + "Waiting for clients on port " + +cPort);
			
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		
	  }
	  
	  public void run() throws IOException{
		
		  while(true){
			 
			  newClientS = mySocket.accept();
			 if(newClientS!=null){
				
				 System.out.println("COnnecting creating new THread");
				 activeClients[clientsNum] = new ClientThread(clientsNum,newClientS,activeClients);
				 activeClients[clientsNum].start();
				 clientsNum++;
				 newClientS = new Socket();
					
			 }
			
			 
			 }
				
	  }
	  

	

		
	  
	  public static void main(String [] args){
		 int sNum =0;
		 Server s = new Server(sNum);
		 sNum++;
		try {
			s.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		 
	  }
	 
	  
	  
	  class ClientThread extends Thread implements Serializable {
		  
		  
		    /**
		 * 
		 */
		private static final long serialVersionUID = 2152505504829929343L;
			/**
		 * 
		 */
			protected String clientName,myMsg;
			protected int clientNumber;
		    protected Socket mySocket,myObjSocket;
			protected DataInputStream input,mI;
			protected DataOutputStream output,mO;	
			 protected  ClientThread[] active = new ClientThread[maxClients];
				
			public ClientThread( int cNum , Socket myCS ,ClientThread[] active ) {
				this.clientNumber = cNum;
			
				this.active =active;
				try {
					
					mySocket = myCS;
					input = new DataInputStream( myCS.getInputStream());
					output = new DataOutputStream( myCS.getOutputStream());
					mI =  new DataInputStream( masterS.getInputStream());
					mO = new DataOutputStream(masterS.getOutputStream());
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
					
					try {
						this.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			public void run(){
				String [] msgSplit;
				
				
				
				recive("Welcome to X_O Secure chat room");
				
				
				while(true){
				
						
					
						 try {
						
							 if(input.available()>1){
								myMsg= (String) input.readUTF();
								msgSplit = myMsg.split("-");
								if(msgSplit.length>0)
								switch(msgSplit[0]){
								case "UserName" : this.clientName = msgSplit[1];
								System.out.println(msgSplit[1]);addUser(clientName);recive(clientName);break;
								
								
								case "Public": sendPublicMessage(clientName+"-"+msgSplit[1]);break;
								
								case "Private" :sendMessageTo(msgSplit[1],msgSplit[2]) ;break;
								
								
								case "ShowMem": sendMembers();break;
								
								case "TM" : sM(msgSplit[1]);break;
								default:System.out.println(myMsg);break;
								
								
					
						
					}
							 }//check for master 
							 if(mI.available()>0){
								 System.out.println("MAster Server request");
								 String masterReq = mI.readUTF();
								 String [] reqSplit = masterReq.split("-");
								 switch(reqSplit[0]){
								 case "GetMembers" :System.out.println("RecivedMastre"); sendMemList();break;
								 case "mL" :this.recive(reqSplit[1]);System.out.println("Se3nding mebers LIst to client "+this.clientName);break;
								 case "PrMsg": sendMessageTo(reqSplit[1],reqSplit[2]);
								 default:System.out.println("Default Master case "+ masterReq );break;
								 }
								 
							 }
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}
			
			}
			
			
	  }
			

			private void sendMessageTo(String string, String string2) {
				boolean notFound=true;
			
				active = activeClients;
				for(ClientThread c : active)
					if(c!=null)
					if(c.clientName.equals(string)){
						c.recive(string2);
						notFound =false;
					}
				if(notFound)
					sM("Private-"+string+"-"+string2);
				
			}

			private synchronized void  sendMembers() {
				active = activeClients;
				for(ClientThread c : active)
				if(this !=c)
					if(c!=null)
					this.recive(c.clientName);
			}

			private void addUser(String name) {
				
				synchronized (this) {
					active = activeClients;
			        for (int i = 0; i <  activeClients.length; i++) {
			          if (activeClients[i] != null && activeClients[i] != this) {
			        	  activeClients[i].recive("*** A new user " + name
			                + " entered the chat room !!! ***");
			          }
			        }
			      }
			}

			private void sendPublicMessage(String msg) throws IOException{
				System.out.println("sending public message");
				 synchronized(this){
					 active = activeClients;
					 for(int i =0 ; i<active.length;i++)
						 if(active[i]!=null){
						 active[i].recive(msg);
						 System.out.println(i+"active clients");
						 }
					 
				 }
				 
				
			}
			
			
			
			private void recive(String msg)  {
				
				try {
					output.writeUTF(msg);
					output.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			private void sM(String msg) {
				System.out.println("Sendingtomaster " +msg);
				try {
					mO.writeUTF(msg);
					mO.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			private synchronized void sendMemList() {
				active = activeClients;
				String l ="";
				System.out.println(l);
		for(ClientThread c : activeClients)
			if(c!=null)
				l+="  "+c.clientName+"  ";
sM(l);			

		}
			
		
			public void close() throws IOException{
				if(mySocket!=null){
					input.close();
					output.close();
					mySocket.close();
				}
			}
			
	  }
	  
}
