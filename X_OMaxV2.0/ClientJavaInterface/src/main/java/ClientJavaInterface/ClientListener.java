package ClientJavaInterface;

import xoLib.Exceptions.MessageCouldnotBeEncryptedException;

public interface ClientListener {
 void onSend() throws MessageCouldnotBeEncryptedException;
void Register(String userName);


}
