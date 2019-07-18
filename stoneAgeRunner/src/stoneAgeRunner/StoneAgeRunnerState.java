package stoneAgeRunner;

import java.util.HashMap;


public class StoneAgeRunnerState extends State{
	
	int runnerX;
	int runnerY;
	Stone[] stones;
	public StoneAgeRunnerState(String[][] map,int x,int y,Stone[] stones) {
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
		
		StoneAgeRunnerState s = p.moveUp("plr", this);
		if(s!=null)
			nextStates.put("moveUp,plr", s);
		
		 s=p.moveDown("plr", this);
		if(s!=null)
			nextStates.put("moveDown,plr", s);
		
		for(int i=0;i<stones.length;i++) {
			s=null;
			
			s = p.moveUp(stones[i].toString(), this);
			if(s!=null)
				nextStates.put("moveUp,"+stones[i].toString(), s);
		}
		for(int i=0;i<stones.length;i++) {
			s=null;
			 s = p.moveDown(stones[i].toString(), this);
			if(s!=null)
				nextStates.put("moveDown,"+stones[i].toString(), s);
			
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
				case "E":s+="  E  ";break;
				case "plr":s+=" plr ";break;
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
				if(!(state[1][j].equals("E")||state[1][j].equals("plr"))) {
					ne++;
				}
			}
			ne+= this.state[1].length- this.runnerY;
			return ne;}
		return 0;
		
	}
	public  int hashCode() {
		
		int hash =0;
		
		for(int i=0;i<state.length;i++)
			for(int j =0;j< state[0].length;j++) {
				String[] sS = state[i][j].split(" ");
				String h ="";
				
				h+=i;
				h+=j;
				if(sS[0].equals("RVL"))
				h= h  + sS[1] + 300; 
					
				if(sS[0].equals("RVS"))
					h= h + sS[1] + 100; 
				if(sS[0].equals("RHL"))
					h= h + sS[1] +30; 
				if(sS[0].equals("RHS"))
					h= h + sS[1]+5; 
				if(sS[0].equals("plr"))
					h= h + 1000; 
				if(sS[0].equals("E"))
					h= h + 1 ; 
				
				hash+= Integer.parseInt(h);
			}
		
		return hash;
		
		
	}
	

}
