import java.io.Serializable;



public class Request implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = -3452400766159618047L;
String [] headers = new String[4];

public Request (String method){
	addHeader(method);
	
	}
public void addHeader(String header) {
int c=0;
			while(headers[c]!=null){
				c++;
			
			}
			headers[c]= header;
		
	}
	// TODO Auto-generated method stub
	

@Override
public String toString(){
	
	String t="";
	for(String s: headers)
		if(s!=null)
		t+=s+",";
	
	return t;
}
public int getDestination() {
	// TODO Auto-generated method stub
	return 6010;
}

}
