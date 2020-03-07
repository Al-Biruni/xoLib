package Client;

import Commons.Exceptions.MessageCouldnotBeEncryptedException;
import Commons.Message.Message;
import Commons.Message.MessageHandler;
import Commons.Message.MessageType;
import Commons.SecretUser;
import Commons.User;
import SlaveServer.Server;
import View.X_O;

import java.io.BufferedReader;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Client  {

	protected ObjectOutputStream oo;
	protected ObjectInputStream oi;

	protected Socket cS;
	protected String message = "";
	protected String host;
	protected int serverPort;



	protected ClientController clientController;

	protected User server;


	protected SecretUser user;
	protected User[] knownActiveUsers;

	public Client() {


		serverPort = 6000;


		try {
			cS = new Socket("localhost", serverPort);
			oo = new ObjectOutputStream(cS.getOutputStream());
			oi = new ObjectInputStream(cS.getInputStream());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}



	public void run() {
		Message receivedMsg;
		while (true) {
			try {


				receivedMsg = (Message) oi.readObject();

				if (receivedMsg != null)
					if (receivedMsg.TTL > 0) {
						System.out.println("Client.Client recived : \n" + receivedMsg.toString());
						Message.handel(receivedMsg, clientController);
					}

			} catch (EOFException e) {
				closeApp();
				// continue;
				// System.out.println("endofM");
			} catch (IOException e) {

				closeApp();
			} catch (ClassNotFoundException e) {

				e.printStackTrace();
				closeApp();

			}
		}





	}



	void sendMessage(Message m) {

		try {

			oo.writeObject(m);
			System.err.println(m.toString());
			oo.flush();
		} catch (IOException e) {

		}
	}

	void closeApp() {
		try {
			sendMessage(new Message(user,server,user.userName,MessageType.LOGOUT));
			oo.close();
			oi.close();
			cS.close();
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------Encryption handlers----------------------------------------------
	public void encrypt(Message msg) throws MessageCouldnotBeEncryptedException {

		Cipher cipher;
		byte[] iv = new byte[128/8];
		SecureRandom srandom = new SecureRandom();
		srandom.nextBytes(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);


		try {
//encrypt msg body with radom key and iv
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecretKey skey = kgen.generateKey();
			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
			ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
			byte[] encryptedtext = ci.doFinal(msg.msgBody.getBytes("UTF-8"));

			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, msg.receiver.pk);
			//encrypt key with reciver public key to make sure that the reciver is the only one can read it

			byte[] encryptedKey =
					cipher.doFinal(skey.getEncoded());
			byte[] encryptedIV = cipher.doFinal(iv);
			//sign the key with my private key so the reciver be sure that i was the source
		//	encryptedKey = user.sign(encryptedKey);
		//	encryptedIV = user.sign(encryptedIV);

			 msg.enMsg=encryptedtext;
			 msg.key=encryptedKey;
			 msg.iv=encryptedIV;
			 msg.msgBody="";


		} catch (Exception e) {

			throw new MessageCouldnotBeEncryptedException(e);
		}


		// encryptedtext = new String(encrypted);

	}

	public String decrypt(Message msg ) {
		try {

			byte[] key = user.unsign(msg.key);
			//cipher.doFinal(msg.key);
			byte[] iv =user.unsign(msg.iv);
					//cipher.doFinal(msg.iv);
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKey skey = new SecretKeySpec(key,"AES");
			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");

			ci.init(Cipher.DECRYPT_MODE, skey, ivspec);

			byte[] dB = ci.doFinal(msg.enMsg);

			String msgText = new String(dB);
			return msgText;
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "null";

	}
//------------------------------------------------------------------------------------------------------





	public static void main(String[] args) {
		Client c = null;
		c = new Client();

		c.run();

	}




	public void setClientController(ClientController clientController) {
		this.clientController = clientController;
	}
}
