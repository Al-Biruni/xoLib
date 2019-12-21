package stoneAgeRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class StoneAgeRunnerState extends State{
	
	int runnerX;
	int runnerY;
	Stone[] stones;
	public StoneAgeRunnerState(byte[][] map,int x,int y,Stone[] stones) {
		super(map);
		this.runnerX=x;
		this.runnerY=y;
		this.stones=stones;
		
	}
	
	@Override
	public int compareTo(State o) {
		// TODO Auto-generated method stub
		return runnerY-this.state[1].length+1;
	}


	@Override
	protected void genStateSpace() {
		this.nextStates = new HashMap<String, State>();
		StoneAgeProblem p = new StoneAgeProblem();
		//move up player id =1
		StoneAgeRunnerState s = p.moveUp((byte) 1, this);
		if(s!=null)
			nextStates.put("moveUp,plr,1", s);
		// move down player id =1 
		 s=p.moveDown((byte)1, this);
		if(s!=null)
			nextStates.put("moveDown,plr,1", s);
		
		for(int i=0;i<stones.length;i++) {
			s=null;
			
			s = p.moveUp(stones[i].id, this);
			if(s!=null)
				nextStates.put("moveUp,"+stones[i].toString()+","+stones[i].id, s);
		}
		for(int i=0;i<stones.length;i++) {
			s=null;
			 s = p.moveDown(stones[i].id, this);
			if(s!=null)
				nextStates.put("moveDown,"+stones[i].toString()+","+stones[i].id, s);
			
		}
		
		
	}
	public String getblockingStone() {
		for(Stone s :stones) {
			if(runnerY+1<state[0].length)
			if(s.toString().equals(this.state[runnerX][runnerY+1])) {
				return s.toString();
			}
		}
		return "NOt";
		
	}

	@Override
	public String toString() {
		String s="";
		for(int i=0;i<state.length;i++) {
			for(int j=0;j<state[0].length;j++) {
				switch(state[i][j]) {
				case 0:s+="  E  ";break;
				case 1:s+=" plr ";break;
				default:s+=state[i][j];
				}
				
			}
			s+="\n";
		}
		return s;
			
	}

	public int estimateGoal(Boolean fun1) {
		// TODO Auto-generated method stub
		if(fun1) {
		int d = this.state[1].length- this.runnerY;
		return d;}
		if(!fun1) {
			int ne=0;
			for(int j =0;j<this.state[1].length;j++) {
				if(!(state[1][j]==0||state[1][j]==1)) {
					ne++;
				}
			}
			ne+= this.state[1].length- this.runnerY;
			return ne;}
		return 0;
		
	}
	public  String hash() {
		
	
	     
		
		int hash=0;
	String h ="";
		
		for(int i=0;i<state.length;i++)
			for(int j =0;j< state[0].length;j++) {
				h+=state[i][j];
			}
		
		
		 try { 
			  
	            // Static getInstance method is called with hashing MD5 
	            MessageDigest md = MessageDigest.getInstance("MD5"); 
	  
	            // digest() method is called to calculate message digest 
	            //  of an input digest() return array of byte 
	            byte[] messageDigest = md.digest(h.getBytes()); 
	  
	            // Convert byte array into signum representation 
	            BigInteger no = new BigInteger(1, messageDigest); 
	  
	            // Convert message digest into hex value 
	            String hashtext = no.toString(16); 
	            while (hashtext.length() < 32) { 
	                hashtext = "0" + hashtext; 
	            } 
	            return (hashtext); 
	        }  
	  
	        // For specifying wrong message digest algorithms 
	        catch (NoSuchAlgorithmException e) { 
	            throw new RuntimeException(e); 
	        }
		
		
		
	}
	

}
