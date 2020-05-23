package xoLib.Message;

import xoLib.User;

public class ClientMessage extends Message {

    public ClientMessage(User sender, User receiver, String msg, MessageType msgType) {
        this.sender = sender;
        this.receiver = receiver;
        this.msgBody = msg;
        this.messageType = msgType;
    }

    public ClientMessage(User s, String msg) {
        this.sender = s;
        this.msgBody = msg;
        this.messageType = messageType.PUBLIC;
    }
}
