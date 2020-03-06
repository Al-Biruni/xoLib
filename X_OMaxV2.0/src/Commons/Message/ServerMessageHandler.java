package Commons.Message;

public interface ServerMessageHandler {
    abstract void sendToAll(Message masterReq);
    abstract void sendPrivateMessage(Message masterReq);
    abstract void RegisterUser();

}
