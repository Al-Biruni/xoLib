import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;


public class Client implements ClientListener{
	protected  DataOutputStream o;
	protected  DataInputStream i;
	protected  Socket cS;
	protected  String message ="";
	protected String host;
	protected int serverPort;
	private String userName;
	private X_O win;
    protected static	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	
	
	public Client(String userName){
		 win = new X_O(); 
		 win.setVisible(true);
		 win.setAlwaysOnTop(true);
		 win.setSize(400, 400);
		 win.setCl(this);
		
		
		
		this.userName = userName;
		serverPort = 6000;
		try{
			cS = new Socket("localhost",serverPort);
			o = new DataOutputStream(cS.getOutputStream());
			i = new DataInputStream(cS.getInputStream());
			sendMessage("UserName-" + userName);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		
		
		
	}
	Thread sendMsg;
	Thread reciveMsg;
	public void run() throws ClassNotFoundException, IOException{
	
	
	
		

 sendMsg = new Thread(){
		String msg = "";
		public void run(){
			while(!(message).equals("quit")){
				try {
					msg= input.readLine();
					sendMessage(msg);
				} catch (IOException e) {
				closeApp();
					e.printStackTrace();
				}
			if(msg.equals("quit")){
				
				closeApp();
			}
				
		}
		}
	};
		 reciveMsg = new Thread(){
			String recivedMsg=" " ;
			public void run(){
			try {
				while(!recivedMsg.equalsIgnoreCase("quit")){
					if(i!=null)
				recivedMsg = i.readUTF();
				System.out.println(recivedMsg);//show Message
				win.txtpnMessagearea.setText(win.txtpnMessagearea.getText()+"\n"+recivedMsg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				closeApp();
			}
			
			
			}
			
		};
		
sendMsg.start();
reciveMsg.start();


	}
	private void sendMessage(String msg) {

		try{
			o.writeUTF(msg+"-TTL-3");
			o.flush();
			//showMessage("\nClient- " + message);
		}catch(IOException e ){
			
		}
	}
	private void closeApp() {
		try{
			o.close();
			i.close();
			cS.close();
			System.exit(0);
			sendMsg.suspend();
			reciveMsg.suspend();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	public static void main(String [] args) {
		System.out.println("Welcome to the X_O v2.0 secureChat application please type a user name :" );
		
		
		try {
			String uN = input.readLine();
			Client c = new Client(uN);
			c.run();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	@Override
	public void onSend() {
		String msg= win.txtEnterMessage.getText();
		if(!msg.equals("quit")){
		sendMessage(msg);
		win.txtEnterMessage.setText("");
		}else{
			win.setVisible(false);
			closeApp();
		}
	}
	@Override
	public void onRecive(String recivedMsg) {
		// TODO Auto-generated method stub
		
	}
	
	
}
