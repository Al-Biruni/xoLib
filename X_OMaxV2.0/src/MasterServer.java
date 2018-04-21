
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;




public class MasterServer {
	  protected   Socket ss,oS;
	  protected DataInputStream  i;
	  protected  DataOutputStream o;
	  protected static   ServerSocket Mserver;
	  protected int port;
	  protected final static int maxServers =4;
	  protected  ServerThread[] serverThreads = new ServerThread[maxServers];
	  
	  
	  public static void main(String []args){
		  int serverPort = 6500;
		  int objPort = 6501;
		  String host = "localhost";
		  
		  
		  try {
		        Mserver = new ServerSocket(6500);
		        MasterServer s = new MasterServer();
		        s.run();
		 
		      } catch (IOException e) {
		        System.out.println(e);
		      }
	  }
		  
		public void run() throws IOException{  
		  while(true){
			  
			
				  ss=Mserver.accept();
				
				  if(ss!=null){
				  int serverNumber = 0;
				  for(serverNumber = 0 ; serverNumber < maxServers ; serverNumber++){
					  if(serverThreads[serverNumber] == null){
						  System.out.println("Server conected......."+serverNumber);
						  (serverThreads[serverNumber] = new ServerThread(ss,serverNumber)).start();
						  break;
					  }else if (serverNumber == maxServers) {
				          ss.close();
				        }
				      }
				  ss=new Socket();
		  
		  
		  }
		  
		  
				  }
		}
				


class ServerThread extends Thread{
	protected int serverNumber;
	protected Socket s;
	protected ServerSocket serverSocket;
	protected Socket connToM;
	protected DataInputStream mI;
	protected DataOutputStream  mO;
	
	protected ObjectInputStream oI;
	protected ServerThread[] runningServers =new ServerThread[maxServers];;
	
	

	 ServerThread(Socket ss,int serverNumber) {
		this.connToM =ss;
		this.serverNumber=serverNumber;
		runningServers=serverThreads;
		try{
		this.mI=new DataInputStream(ss.getInputStream());
		this.mO= new DataOutputStream(ss.getOutputStream());
		
		
		
		}catch(IOException e){
			e.printStackTrace();
			closeThread();
			
			
		}
	
	}
	
	public void run(){
		
		
			String serverRequest ="";
			String [] reqSplit;
			while(true ){
				try {
					serverRequest= (String) mI.readUTF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				
				reqSplit = serverRequest.split("-");
				switch(reqSplit[0]){
				case "getAllMembers": this.sendToServer("mL-"+getAllMembers());break;
				case "Private": for(ServerThread sT : serverThreads )
					                 if(sT!=null)
					    if(this!=sT)
					    	sT.sendToServer("PrMsg-"+reqSplit[1]+"-"+reqSplit[2]);
				System.out.println(reqSplit[1]+"-"+reqSplit[2]);break;
				
				
				
				default:break;
				}
			}
		
		
		
	}
	
	
	private void sendToServer(String msg) {
		try {
			mO.writeUTF(msg);
			mO.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private synchronized String getAllMembers(){
		runningServers = serverThreads;
		String r ="";
		ArrayList<String >temp = new ArrayList<String>() ; 
		for(ServerThread st : runningServers){
			if(st!=null){
				System.out.println("looping on servers " + st.serverNumber);
		temp.add(st.getUserArray());
			}
		}
		for(String t : temp)
			if(t!=null)
				r+=t+ "  ";
		System.out.println(r);
		return r;
	}
	
	
	


	private String  getUserArray() {
		try {
			System.out.println("Getting Members");
			this.mO.writeUTF("GetMembers-X");
			this.mO.flush();
		
			String list = (String) this.mI.readUTF();
			return list;
		
	}catch(Exception e ){
		System.out.println(e.getMessage());
		}
		return null;
	}

	private synchronized void closeThread() {
		// TODO Auto-generated method stub
		
		try {
			mI.close();
			mO.close();
			oI.close();
			s.close();
			for(int i = 0 ; i <runningServers.length;i++){
				if(runningServers[i]==this){
					runningServers[i]=null;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	

}}
