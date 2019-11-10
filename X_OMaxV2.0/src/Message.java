import java.io.Serializable;

public class Message implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2976187782154270436L;
	User sender=null,receiver=null;
	

	
	String msgBody="";
	User [] users;
	byte[] enMsg;
	byte[] key;
	byte[]iv;
	int TTL=4;
	MessageType messageType;
	
	
	public Message(User s,User r,String msg, MessageType msgType) {
		this.sender=s;
		this.receiver=r;
		this.msgBody=msg;
		this.messageType=msgType;
	}
	
	public Message(User s,String msg) {
		this.sender=s;
		this.msgBody=msg;
		this.messageType=messageType.PUBLIC;
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
	
		
				m+= "\nMsgbody: "+msgBody+"\nMessage type: "+messageType;
				m+="\n TTL: "+TTL;
		return m;
		
	}
	

}
