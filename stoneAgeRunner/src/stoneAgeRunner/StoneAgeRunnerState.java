package stoneAgeRunner;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;


public class StoneAgeRunnerState extends State{
	
	byte runnerX;
	byte runnerY;
	Stone[] stones;
	public StoneAgeRunnerState(byte[][] map,int x,int y,Stone[] stones) {
		super(map);
		this.runnerX=(byte)x;
		this.runnerY=(byte)y;
		this.stones=stones;
		
	}
	
	@Override
	public int compareTo(State o) {
	//goal state 
		return runnerY-this.state[1].length+1;
	}




	public String getblockingStone() {
		for(int i=runnerY;i<state[1].length;i++) {
			if(state[1][i] != 0&& state[1][i]!=1) {
				String sb ="";
				sb+=state[1][i];
				return sb;
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
				default:
					
					s+=state[i][j];
				}
				
			}
			s+="\n";
		}
		return s;
			
	}

	public int estimateGoal(Boolean fun1) {
		
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





}
