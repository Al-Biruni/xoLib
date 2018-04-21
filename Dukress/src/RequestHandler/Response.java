
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;




public class Response implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 7601095247668569795L;
public String Status , Format,Connection;
public String timeStamp;
public byte[] data = null;

public Response(String s , String f , String c ){
	this.timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
	this.Status = s;
	this.Format = f;
	this.Connection = c;
	
}

public void setData(byte[] d){
	this.data = d;
}

@Override
public String toString(){
	String t = "" ;
	
	t+=Status+"\n";
	t+=timeStamp+"\n";
	t+=Format+"\n";
	t+=Connection+"\n";
	
	
	
	return t;
	
}

}
