

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.swing.JTextPane;

public class Browser extends Thread{
	protected ObjectOutputStream o;
	protected  ObjectInputStream i;
	protected  Socket cS;
	protected  StringBuilder request;
	protected String host;
	protected int serverPort;
	private String userName;
	private JTextPane resArea;
	Thread reciver;
	public Browser(JTextPane resArea){
		Scanner sc= new Scanner(System.in);
	    System.out.println("please enter a user name for browser ");
		this.userName = sc.next();
	    this.resArea = resArea;
	 
		

	}
	public void run (){
		





				reciver = new Thread(){
					
					public void run(){
					
					try {
						
						while(true){
						
						System.out.println("response came ");
						Response r = null ;
						
							
								
						 r =(Response ) i.readObject();
						
						 resArea.setText(r.toString());
						 System.out.println(r.toString());
						OutputStream out = null;
						
						try {
							out = new FileOutputStream("test2."+r.Format);
						} catch (FileNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						};
						
					
						
						byte[] bytes = r.data;
						 int count;
					        while ((count = i.read(bytes)) > 0) {
					            out.write(bytes, 0, count);
					        }
							
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					
					
					}};
				
				}
			
			
			
			
		

		
		
		
		
	
	
	
	

	public void sendRequest(Request r ){
		
		if(cS==null){
			try{
				cS = new Socket("localhost",r.getDestination());
				o = new ObjectOutputStream(cS.getOutputStream());
				i = new ObjectInputStream(cS.getInputStream());
				reciver.start();
				try {
					
					o.writeObject(r);
					o.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}else{
			try {
				
				o.writeObject(r);
				o.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	
	

	
	
}
