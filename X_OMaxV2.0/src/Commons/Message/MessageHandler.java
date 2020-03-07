package Commons.Message;

import Commons.User;

public interface MessageHandler {


    abstract void register(Message registerMessage);
    abstract void publicMessage(Message msg);
    abstract void privateMessage(Message msg);


    abstract void onlineUsersRequest(Message message);

    void sendToAll(Message message);

    void logout(Message msg);

    void getAllUsers(Message msg);

    void newUser(Message msg);
}
