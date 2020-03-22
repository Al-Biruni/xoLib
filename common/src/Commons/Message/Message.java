package Commons.Message;

import Commons.User;

import java.io.Serializable;

public class Message implements Serializable  {

	private static final long serialVersionUID = 2976187782154270436L;
	public User sender=null,receiver=null;
	

	public String msgBody="";


	public byte[] enMsg;
	public byte[] key;
	public byte[]iv;

	public User[] users;

	public int TTL=4;
	public MessageType messageType;

	
	
	public Message(User sender, User receiver, String msg, MessageType msgType) {
		this.sender=sender;
		this.receiver=receiver;
		this.msgBody=msg;
		this.messageType=msgType;
	}
	
	public Message(User s, String msg){
		this.sender=s;
		this.msgBody=msg;
		this.messageType=messageType.PUBLIC;
	}


	public Message createOnlineUsersMessage(User s , User[] onUsers){
		Message msg = new Message(s ,null,"",MessageType.ONLINEUSERS);
		msg.users = onUsers;
		return msg;

	}
	
	//clone constructor 
	public Message(Message msg) {
		this.sender=msg.sender;
		this.receiver=msg.receiver;
		this.enMsg=msg.enMsg;
		this.messageType=msg.messageType;
		this.msgBody=msg.msgBody;
		this.users=msg.users;
		this.TTL=msg.TTL;
		this.iv=msg.iv;
		this.key=msg.key;
	}



	public static void handel(Message msg, MessageHandler handler) {


		switch (msg.messageType) {

			case GETALLUSERS:handler.getAllUsers(msg);
				break;
			case ONLINEUSERS:
				handler.onlineUsersRequest(msg);

				break;
			case PRIVATE:
				handler.privateMessage(msg);
				break;
			case PUBLIC:
				handler.sendToAll(msg);
				break;
			case REGISTER:
				handler.register(msg);
				break;
			case LOGOUT:
				handler.logout(msg);
				break;
			case NEWUSER:handler.newUser(msg);
				break;
			default:
				System.out.println("Default handling case " + msg);
				break;

		}


	}

	public void decTTL() {
		TTL--;
	}
	
	@Override
	public String toString() {
		String m="";
		if(sender!=null)
		m+="Sender: "+sender.userName ;
		if(receiver!=null)
		m+= "Reciever: "+receiver.userName;
	
		
				m+= "\nMsgbody: "+msgBody+"\n Message type: "+messageType;
				m+="\n TTL: "+TTL;
		return m;
		
	}
	

}
