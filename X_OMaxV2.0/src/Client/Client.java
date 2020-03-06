package Client;

import Commons.Message.Message;
import Commons.Message.MessageType;
import Commons.User;
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

public class Client implements ClientListener {

	protected ObjectOutputStream oo;
	protected ObjectInputStream oi;

	protected Socket cS;
	protected String message = "";
	protected String host;
	protected int serverPort;

	protected User server;

	private static X_O win;
	protected static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	public PublicKey pbKey;
	protected PrivateKey prKey;
	protected Signature rsa;
	protected User user;
	protected User[] knownActiveUsers;

	public Client() {

		win = new X_O(this);
		serverPort = 6000;
		win.regDialog();

		try {
			cS = new Socket("localhost", serverPort);
			oo = new ObjectOutputStream(cS.getOutputStream());
			oi = new ObjectInputStream(cS.getInputStream());

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	Thread receiveMsg;

	public void run() throws ClassNotFoundException, IOException {

		receiveMsg = new Thread() {
			Message receivedMsg;

			public void run() {
				while (true) {
					try {

						receivedMsg = (Message) oi.readObject();

						if (receivedMsg != null)
						if(receivedMsg.TTL>0){
							System.out.println("Client.Client recived : \n" + receivedMsg.toString());
							switch (receivedMsg.messageType) {

							case PUBLIC:
								win.msgArea.setText(win.msgArea.getText() + "\n " + receivedMsg.sender.userName + " : "
										+ receivedMsg.msgBody);
								break;
							case PRIVATE:
								String dMsg = decrypt(receivedMsg);
								win.msgArea.setText(
										win.msgArea.getText() + "\n " + receivedMsg.sender.userName + " : " + dMsg);
								break;
							case ONLINEUSERS:
								User[] onUsr = receivedMsg.users;
								for (User u : onUsr)
									if (u != null)
										win.listModel.addElement(u);
								// System.out.println(u.userName);
								break;
							case REGISTER:

					
									if (receivedMsg.msgBody.equals("true")) {

										win.userReg.setVisible(false);
										win.chatView();

										Message getMembers = new Message(user, server, "", MessageType.GETALLUSERS);
										sendMessage(getMembers);

									} else {
										win.usrNameText.setText("Choose diffrentuser name");

									}
							

								break;
							case LOGOUT:for(int i=0;i<win.listModel.getSize();i++) {
								if(win.listModel.get(i).userName.equals(receivedMsg.msgBody)) {
									win.listModel.remove(i);
									break;
								}
							}
								
							case NEWUSER:
								win.listModel.addElement(receivedMsg.receiver);
							

							break;
							default:
								System.out.println(receivedMsg.msgBody + "  " + receivedMsg.messageType);
								break;

							}
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

		};

		receiveMsg.start();

	}

	private void sendMessage(Message m) {

		try {

			oo.writeObject(m);
			System.err.println(m.toString());
			oo.flush();
		} catch (IOException e) {

		}
	}

	private void closeApp() {
		try {
			oo.close();
			oi.close();
			cS.close();
			System.exit(0);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// ----------------------------------Encryption handlers----------------------------------------------
	public void encrypt(Message msg) {

		Cipher cipher;
		byte[] iv = new byte[128/8];
		SecureRandom srandom = new SecureRandom();
		srandom.nextBytes(iv);
		IvParameterSpec ivspec = new IvParameterSpec(iv);
		

		try {

			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			SecretKey skey = kgen.generateKey();
			Cipher ci = Cipher.getInstance("AES/CBC/PKCS5Padding");
			ci.init(Cipher.ENCRYPT_MODE, skey, ivspec);
			byte[] encryptedtext = ci.doFinal(msg.msgBody.getBytes("UTF-8"));
			
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, msg.receiver.pk);

			byte[] encryptedKey = cipher.doFinal(skey.getEncoded());
			byte[] encryptedIV = cipher.doFinal(iv);
			 msg.enMsg=encryptedtext;
			 msg.key=encryptedKey;
			 msg.iv=encryptedIV;
			 msg.msgBody="";
		
			
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
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// encryptedtext = new String(encrypted);

	}

	public String decrypt(Message msg ) {
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			//make sure it is only read by  me 
			cipher.init(Cipher.DECRYPT_MODE, this.prKey);

			byte[] key = cipher.doFinal(msg.key);
			byte[] iv = cipher.doFinal(msg.iv);
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
		}
		return "null";

	}
//------------------------------------------------------------------------------------------------------

	@Override
	public void onSend() {
		Message m = null;

		String msg = win.msgText.getText();

		if (win.rdbtnPrivate.isSelected()) {
			User receiver = win.listModel.get(win.list.getSelectedIndex());
			m = new Message(this.user, receiver, msg, MessageType.PRIVATE);
			//byte[] eMsg = 
					encrypt(m);

			
			//m.enMsg = eMsg;
		} else {
			m = new Message(user, msg);
		}

		win.msgText.setText("");
		sendMessage(m);

		if (msg.equals("quit")) {
			win.setVisible(false);
			closeApp();

		}
	}

	@Override
	public void Register(String userName) {
		KeyPairGenerator keyGen;

		try {
			keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair pair = keyGen.generateKeyPair();
			pbKey = pair.getPublic();
			prKey = pair.getPrivate();

			user = new User(userName, pbKey);
			Message m = new Message(user, null, "", MessageType.REGISTER);

			sendMessage(m);

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		Client c = null;
		c = new Client();

		try {
			c.run();
		} catch (ClassNotFoundException | IOException e) {

			e.printStackTrace();
		}

	}

}
