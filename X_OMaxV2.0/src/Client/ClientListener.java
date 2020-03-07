package Client;

import Commons.Exceptions.MessageCouldnotBeEncryptedException;

public interface ClientListener {
 void onSend() throws MessageCouldnotBeEncryptedException;
void Register(String userName);


}
