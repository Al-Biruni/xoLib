package Commons.Message;

import Commons.User;

public class MessageFactroy {
    public MessageFactroy(){}

    public Message CreateMessage(User sender, User reciver ,String msgBody, MessageType msgType){

        switch (msgType){

            case PUBLIC: retrun new PublicMessage(sender,reciver,msgBody);
                break;
            case PRIVATE:retrun new PrivateMessage(sender,reciver,msgBody);
                break;
            case GETALLUSERS:
                break;
            case ONLINEUSERS:
                break;
            case REGISTER:
                break;
            case LOGOUT:
                break;
            case NEWUSER:retrun new PublicMessage(sender,reciver,msgBody);
                break;
        }


    }


}
