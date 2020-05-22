package xoLib;

import java.io.Serializable;
import java.security.PublicKey;

public class User implements Serializable  {

	private static final long serialVersionUID = 5257202750799314168L;
	public String userName;
	public PublicKey publicKey;
	
	public User(String uN,PublicKey publicKey) {
		this.userName=uN;
		this.publicKey = publicKey;
		
	}

	@Override
	public String toString() {
		return userName;
	}
	
	

}
