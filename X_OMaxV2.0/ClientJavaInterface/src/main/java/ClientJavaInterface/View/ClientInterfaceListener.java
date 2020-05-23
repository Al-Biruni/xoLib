package ClientJavaInterface.View;

import xoLib.Exceptions.MessageCouldnotBeEncryptedException;

public interface ClientInterfaceListener {
 void onSend() throws MessageCouldnotBeEncryptedException;
void onRegisterBtnClick(String userName);


}
