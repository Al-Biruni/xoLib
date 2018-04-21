package secProj;

import java.security.PublicKey;

public interface TransactionListener {
	
	public void makeTransaction(byte[] eData, PublicKey pbKey );
	

}
