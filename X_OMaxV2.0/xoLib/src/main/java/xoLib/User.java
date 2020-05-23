package xoLib;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.*;

public class User implements Serializable  {

	private static final long serialVersionUID = 5257202750799314168L;
	public String userName;
	public PublicKey publicKey;
	
	 User(String uN,PublicKey publicKey) {
		this.userName=uN;
		this.publicKey = publicKey;
		
	}

	public boolean verify(byte[] plainBytes, byte[] signedBytes) throws Exception {
		try{

			Signature publicSignature =Signature.getInstance("SHA256withRSA");
			publicSignature.initVerify(publicKey);
			publicSignature.update(plainBytes);

			return publicSignature.verify(signedBytes);
		}catch (Exception e){
			throw new Exception("Verifying Signature failed");
		}

	}

	public byte[] encryptWithPublicKey(byte[] encryptedBytes) throws Exception {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);

			return cipher.doFinal(encryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		throw new Exception("message couldn't be decrypted Exception");

	}

	public byte[] decryptWithPublicKey(byte[] encryptedBytes) throws Exception {
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);

			return cipher.doFinal(encryptedBytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		throw new Exception("message couldn't be decrypted Exception");

	}

	@Override
	public String toString() {
		return userName;
	}


}
