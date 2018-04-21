package secProj;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Random;

import javax.crypto.Cipher;

public class Node implements TransactionListener {
	public PublicKey pbKey;
	public TransactionListener tL;
	protected Transaction[] blockTransactions = new Transaction[10];
	protected ArrayList<Node> myN = new ArrayList<Node>();
	protected PrivateKey prKey;
	protected Signature rsa;

	public Node() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
			keyGen.initialize(1024);
			KeyPair pair = keyGen.generateKeyPair();
			pbKey = pair.getPublic();
			prKey = pair.getPrivate();

			tL = this;

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void connectToNode(Node n) {

		myN.add(n);
	}

	private void validTransaction(Transaction t) {
		System.out.println("Transaction input :-" +t.input+"  SOurce ");
		// if valid add to block

	}

	public byte[] encrypt(byte[] message) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, this.prKey);

		return cipher.doFinal(message);
	}

	public static byte[] decrypt(PublicKey publicKey, byte[] encrypted)
			throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);

		return cipher.doFinal(encrypted);
	}

	@Override
	public void makeTransaction(byte[] eT, PublicKey source) {
		byte[] pByte;
		ByteArrayInputStream bis = null;
		try {
			pByte = decrypt(source, eT);
			bis = new ByteArrayInputStream(pByte);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			Transaction o = (Transaction) in.readObject();
			System.out.println("Node :-"+this.pbKey.toString());
			validTransaction(o);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				// ignore close exception
			}
		}

	}

	public void annonceTransaction(Transaction x) {
		// TODO Auto-generated method stub
		// encrypt
		ByteArrayOutputStream pData = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] bData = null, eBData = null;
		try {
			out = new ObjectOutputStream(pData);
			out.writeObject(x);
			out.flush();
			bData = pData.toByteArray();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				pData.close();
			} catch (IOException ex) {
				// ignore close exception
			}
		}

		try {
			eBData = encrypt(bData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// random

Random rand = new Random();
ArrayList<Integer> seenInt = new ArrayList<Integer>();
int  r = rand.nextInt(myN.size()-1);
int gosip = rand.nextInt(myN.size()-1);
	while(seenInt.size()<gosip){
		
		if(!seenInt.contains(r)){
			myN.get(r).tL.makeTransaction(eBData, this.pbKey);
			seenInt.add(r);
			//System.out.println(myN.get(r).pbKey.toString());
			}else
		 r = rand.nextInt(myN.size()-1);
		
		
		}
	}

}
