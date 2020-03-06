package Commons.Message;

public interface MessageHandler {
    abstract void sendPublicMessage(Message masterReq);
    abstract void sendPrivateMessage(Message masterReq);
}
