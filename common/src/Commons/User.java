package Commons;

import java.io.Serializable;
import java.security.PublicKey;

public class User implements Serializable  {

	private static final long serialVersionUID = 5257202750799314168L;
	public String userName;
	public PublicKey pk;
	
	public User(String uN,PublicKey pk) {
		this.userName=uN;
		this.pk=pk;
		
	}

	@Override
	public String toString() {
		return userName;
	}
	
	

}
