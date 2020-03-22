package Commons.Exceptions;

public class MessageCouldnotBeEncryptedException extends Exception {
    public MessageCouldnotBeEncryptedException(Exception e){
        super(e.getMessage(),e);
    }
}
