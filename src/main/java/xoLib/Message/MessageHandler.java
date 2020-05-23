package xoLib.Message;


import xoLib.Exceptions.* ;

public interface MessageHandler {


    void register(Message registerMessage) throws NotUniqueUserNameException ;
    void publicMessage(Message msg);
    void privateMessage(Message msg);


    void onlineUsersRequest(Message message);

    void sendToAll(Message message);

    void logout(Message msg);

    void getAllUsers(Message msg);

    void newUser(Message msg);
}
