package xoLib.Message;

import xoLib.Exceptions.MessageCouldnotBeEncryptedException;
import xoLib.Exceptions.MessageIsDeadException;
import xoLib.Exceptions.NotUniqueUserNameException;
import xoLib.SecretUser;
import xoLib.User;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public abstract class Message implements Serializable {

    private static final long serialVersionUID = 2976187782154270436L;
    public User sender = null, receiver = null;
    public String msgBody = "";

    public byte[] enMsg;
    public byte[] key;
    public byte[] iv;
    public byte[] encryptedMessageDigest;

    //for online users and registration process and sending login/out notification
    public User[] users;

    protected int TTL = 4;
    public MessageType messageType;

    //clone
    public Message clone(Message msg) {
        this.sender = msg.sender;
        this.receiver = msg.receiver;
        this.enMsg = msg.enMsg;
        this.messageType = msg.messageType;
        this.msgBody = msg.msgBody;
        this.users = msg.users;
        this.TTL = msg.TTL;
        this.iv = msg.iv;
        this.key = msg.key;

        return this;
    }


    public static void handel(Message msg, MessageHandler handler) throws NotUniqueUserNameException {


        switch (msg.messageType) {

            case GETALLUSERS:
                handler.getAllUsers(msg);
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
            case NEWUSER:
                handler.newUser(msg);
                break;
            default:
                System.out.println("Default handling case " + msg);
                break;

        }


    }

    public void decTTL() throws MessageIsDeadException {
        if (TTL > 0) {
            TTL--;
        } else {
            throw new MessageIsDeadException();
        }
    }

    // ----------------------------------Encryption handlers----------------------------------------------
    public static void encrypt(Message msg, SecretUser sender) throws MessageCouldnotBeEncryptedException {

        Cipher cipher;
        byte[] iv = new byte[128 / 8];
        SecureRandom srandom = new SecureRandom();
        srandom.nextBytes(iv);
        IvParameterSpec ivspec = new IvParameterSpec(iv);


        try {
            //encrypt msg body with random key and iv
            KeyGenerator keyGeneator = KeyGenerator.getInstance("AES");
            SecretKey secretKey = keyGeneator.generateKey();

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            byte[] encryptedText = cipher.doFinal(msg.msgBody.getBytes("UTF-8"));

            //encrypt key with receiver public key to make sure that the receiver is the only one can read it
            byte[] encryptedKey = msg.receiver.encryptWithPublicKey(secretKey.getEncoded());
            byte[] encryptedIV = msg.receiver.encryptWithPublicKey(iv);

            //sign the hash code with sender private key
            // so the receive be sure that i was the source and that the message didn't change

            int encryptedTextHashCode = encryptedText.hashCode();
            byte[] etcBuffer = ByteBuffer.allocate(4).putInt(encryptedTextHashCode).array();
            byte[] encryptedHash = sender.sign(etcBuffer);


            msg.enMsg = encryptedText;
            msg.key = encryptedKey;
            msg.iv = encryptedIV;
            msg.msgBody = "";
            msg.encryptedMessageDigest = encryptedHash;


        } catch (Exception e) {
            e.printStackTrace();
            throw new MessageCouldnotBeEncryptedException(e);
        }

    }

    public static String decrypt(Message msg, SecretUser receiver) throws Exception {

        if (validMessageDigest(msg)) {

            try {
                byte[] key = receiver.decryptDataWithPrivateKey(msg.key);
                byte[] iv = receiver.decryptDataWithPrivateKey(msg.iv);

                IvParameterSpec ivspec = new IvParameterSpec(iv);

                SecretKey skey = new SecretKeySpec(key, "AES");
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
        throw new Exception("message is broken MessageDigest Validation failed ");
    }

    public static boolean validMessageDigest(Message msg) throws Exception {
        byte[] constructedPlainBytes = ByteBuffer.allocate(4).putInt(msg.enMsg.hashCode()).array();
        return msg.sender.verify(constructedPlainBytes, msg.encryptedMessageDigest);
    }


    public int getTTL() {
        return TTL;
    }

    @Override
    public String toString() {
        String m = "";
        if (sender != null)
            m += "Sender: " + sender.userName;
        if (receiver != null)
            m += "Receiver: " + receiver.userName;


        m += "\nMsgbody: " + msgBody + "\n Message type: " + messageType;
        m += "\n TTL: " + TTL;
        return m;

    }


}
