package xoLib.UnitTests;

import org.junit.jupiter.api.Test;
import xoLib.Message.ClientMessage;
import xoLib.Message.Message;
import xoLib.Message.MessageType;
import xoLib.SecretUser;


import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void encrypt() throws Exception {
        SecretUser sender = SecretUser.generateSecretUser("Sender");
        SecretUser receiver = SecretUser.generateSecretUser("Receiver");

        String privateText = "Private message";


        ClientMessage msg = new ClientMessage(sender, receiver, privateText, MessageType.PRIVATE);
        Message.encrypt(msg, sender);

        int encryptedTextHashCode = msg.enMsg.hashCode();
        byte[] etcBuffer = ByteBuffer.allocate(4).putInt(encryptedTextHashCode).array();

        assertEquals(sender, msg.sender);
        assertTrue(sender.verify(etcBuffer, msg.encryptedMessageDigest));

        assertNotNull(msg.encryptedMessageDigest);
        assertNotNull(msg.enMsg);
        assertNotNull(msg.key);
        assertNotNull(msg.iv);



    }

    @Test
    void decrypt() throws Exception {
        SecretUser sender = SecretUser.generateSecretUser("Sender");
        SecretUser receiver = SecretUser.generateSecretUser("Receiver");

        String privateText = "Private message";
        ClientMessage msg = new ClientMessage(sender, receiver, privateText, MessageType.PRIVATE);
        Message.encrypt(msg, sender);

        String decryptedText = Message.decrypt(msg, receiver);

        assertEquals(privateText, decryptedText);


    }
}