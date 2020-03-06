package Commons.Message;

import Commons.SecretUser;
import Commons.User;

public class PrivateMessage extends Message
{
    public byte[] enMsg;
    public byte[] key;
    public byte[]iv;

    public PrivateMessage(SecretUser sender, User reciver, String msgBody) {



    }
}
