package xoLib;


import xoLib.Exceptions.MessageCouldnotBeEncryptedException;

import javax.crypto.*;


import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

public class SecretUser extends User {
    protected static PrivateKey privateKey;
    protected KeyPair myKey;

    protected SecretUser(String userName, KeyPair keyPair) {
        super(userName, keyPair.getPublic());
        myKey = keyPair;
        this.privateKey = keyPair.getPrivate();

    }

    public static SecretUser generateSecretUser(String userName) throws Exception {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair pair = keyGen.generateKeyPair();

            return new SecretUser(userName, pair);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new Exception("Couldn't initialize keys");
    }

    public byte[] sign(byte[] plainBytes) throws Exception {


        try {
            Signature privateSignature = Signature.getInstance("SHA256withRSA");
            byte[] data = new byte[100];
            privateSignature.initSign(myKey.getPrivate());
            privateSignature.update(plainBytes);


            byte [] signedBytes = privateSignature.sign();

      return signedBytes ;

        } catch (Exception e) {
            e.printStackTrace();
            throw new MessageCouldnotBeEncryptedException(e);
        }

    }

    public byte[] decryptDataWithPrivateKey(byte[] encryptedBytes) throws Exception {


        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedBytes);
        } catch (Exception e) {
            throw new MessageCouldnotBeEncryptedException(e);
        }

    }




}
