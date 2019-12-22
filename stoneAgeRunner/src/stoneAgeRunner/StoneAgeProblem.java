package stoneAgeRunner;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

public class StoneAgeProblem extends Problem {
	
	Random rand = new Random();
	
	
	public StoneAgeRunnerState genGrid() {
		
		int n = rand.nextInt(5) + 5;
		int m = rand.nextInt(5) + 5;
		//byte n = 9;
		//byte m = 15;
		double difficulty =0.76;
		byte[][] grid = new byte[n][m];
		int rY = rand.nextInt(m - 2) + 1;
		grid[1][rY]=1;
		grid[1][rY-1]=1;
		int pX =1;
		int pY = rY;
		
		int rockN = (int)(((m*n)/3)*difficulty);
		//System.out.println(rockN);
	Stone[] stones = new Stone[rockN];
		int i=0;
		
		// generate rocks
			while(i<rockN) {
			
			int randrockPlacment = rand.nextInt(2);
			int randrockLen = rand.nextInt(2)+2;
			int rRx;
			int rRy;
			boolean aFlag = false;
			switch(randrockPlacment) {
			//horizontal case 2-128
			case 0 :
				while(!aFlag) {
				 rRx = rand.nextInt(n);
				 rRy = rand.nextInt(m-randrockLen)+randrockLen;
				 //check empty cells for rock and not to but a H car infront of player rRx!=1
				 boolean f=true;
				 for(int c=0;c<randrockLen;c++) {
					 
					 
					if(grid[rRx][rRy-c]!=0||rRx==1) 
						f=false;
				 }
				 if(!f)
					 continue;
				 
				 aFlag=true;
				 stones[i]= new Stone((byte) (rRx),(byte) rRy, i,false,(byte) randrockLen);
				
				 for(int c=0;c<randrockLen;c++) {
					 switch(randrockLen) {
					 // short H rocks from 2 - 50
					 case 2 :grid[rRx][rRy-c]= (byte) (2+i) ;break;
					 //long H rocks from 50 - 128
					 case 3 :grid[rRx][rRy-c]= (byte) (50+i);break;
					 default:System.err.println("wrong car length : " + randrockLen);
					 }
					
					 } i++;	
				}break;
				//vertical case from -2 - -127
			case 1:
				while(!aFlag) {
				 rRx = rand.nextInt(n-randrockLen)+randrockLen;
				 rRy = rand.nextInt(m);
				 boolean f =true;
				 for(int c=0;c<randrockLen;c++) {
						if(grid[rRx-c][rRy]!=0) 
							f=false;
				 }
				 if(!f)
					 continue;
					 aFlag=true;
					 stones[i]= new Stone((byte) (rRx),(byte) rRy, i,true,(byte) randrockLen);
					
					 for(int c=0;c<randrockLen;c++) {
						 switch(randrockLen) {
						 // short V rocks from -2 - -50
						 case 2 :grid[rRx-c][rRy]= (byte) (-2-i);break;
						 // long V rocks from -50 - -127
						 case 3 :grid[rRx-c][rRy]= (byte) (-50-i);break;
						 default:System.err.println("wrong Stone length : " + randrockLen);
						 }
								
						 } i++;
					}break;
				default: System.err.println("random Stone placment is not 0 or 1 :" + randrockPlacment);
			}
		}
			
			/*
			for(int i1 =0;i1<grid.length;i1++)
				for(int j=0;j<grid[0].length;j++)
					if(grid[i1][j].equals(null))
						grid[i1][j]=0;*/
			
		StoneAgeRunnerState intialState = new StoneAgeRunnerState(grid,pX,pY,stones);
		System.out.println(intialState.toString());
		return intialState;
		
	}

	
	public StoneAgeRunnerState moveUp(byte obj, StoneAgeRunnerState s){
		
		Point objSP = locateObj(obj,s);
		
		byte[][]newGrid = new byte [s.state.length][s.state[0].length];
		for(int i =0;i<newGrid.length;i++)
			for(int j=0;j<newGrid[0].length;j++)
				newGrid[i][j]=s.state[i][j];
		int pX = s.runnerX;
		int pY =s.runnerY;
		Stone [] stones = new Stone[s.stones.length];
for(int i=0;i<stones.length;i++) {
	Stone t = s.stones[i];
	Stone t2 = new Stone(t.x, t.y, t.id, t.vertical, t.length);
	stones[i]=t2;
}
		byte n = (byte) newGrid.length;
		byte m = (byte) newGrid[0].length;
		

		//String[] objDes=obj.split(" ");
		//String objN=objDes[0];
		
		//edge limiters
		
		//if(objN.equals("RVS")||objN.equals("RVL")) vertical move up and x is at the top edge 
		if(obj<-1)
			if(objSP.x==(n-1)) 
				return null;
			
		
		//if(objN.equals("RHS")||objN.equals("RHL")||objN.equals("plr")) horizontal move up and x is at the right edge 
		if(obj>0)
			if(objSP.y==(m-1)) 
				return null;
		
		
		//move function
		
		//case "RVS":
		if(obj<-1 && obj>-50) {
			if( newGrid[objSP.x+1][objSP.y]==0) {
			        newGrid[objSP.x+1][objSP.y]=obj;
		            newGrid[objSP.x-1][objSP.y]=0;
			}else {return null;}
		
	}//case "RVL":
		else if(obj<=-50) {
			if( newGrid[objSP.x+1][objSP.y]==0) {
				newGrid[objSP.x+1][objSP.y]=obj;
			
        newGrid[objSP.x-2][objSP.y]=0;
			}else {return null;}
		}//case "RHS":
		else if(obj>1 && obj<50) {
			if( newGrid[objSP.x][objSP.y+1]==0) {
			newGrid[objSP.x][objSP.y+1]=obj;
        newGrid[objSP.x][objSP.y-1]=0;	}else {return null;}
			
		}	
		//case "RHL":
		else if(obj>=50) {
		if( newGrid[objSP.x][objSP.y+1]==0) {
			newGrid[objSP.x][objSP.y+1]=obj;
        newGrid[objSP.x][objSP.y-2]=0;}else {return null;}
			
		}//case "plr":
		else if(obj == 1)
		if( newGrid[objSP.x][objSP.y+1]==0) {
			newGrid[objSP.x][objSP.y+1]=obj;
        newGrid[objSP.x][objSP.y-1]=0;
        pY+=1;}else {return null;}
			
		
		
		
		//stone location update
		for(Stone st: stones) {
			
			if(st.id==obj) {
				
				if(st.vertical) {
					st.x+=1;}
				else {
					st.y+=1;
				}
			
				
			}
		}
		StoneAgeRunnerState newState = new StoneAgeRunnerState(newGrid,pX,pY,stones);
		return newState;
		
	}
	
	public  StoneAgeRunnerState moveDown(byte obj, StoneAgeRunnerState s){
		
		Point objSP = locateObj(obj,s);
		
		byte[][]newGrid = new byte [s.state.length][s.state[0].length];
		for(int i =0;i<newGrid.length;i++)
			for(int j=0;j<newGrid[0].length;j++)
				newGrid[i][j]=s.state[i][j];
		int pX = s.runnerX;
		int pY =s.runnerY;
		Stone [] stones = new Stone[s.stones.length];
for(int i=0;i<stones.length;i++) {
	Stone t = s.stones[i];
	Stone t2 = new Stone(t.x, t.y, t.id, t.vertical, t.length);
	stones[i]=t2;
}
		
		int n = newGrid.length;
		int m = newGrid[0].length;
		

		//String[] objDes=obj.split(" ");
		//String objN=objDes[0];
		
		//edge limiters
		//if(objN.equals("RVS"))
		if(obj<-1 && obj>-50)
			if(objSP.x-1==0) 		
				return null;
			
		//if(objN.equals("RVL")) 
		if(obj<=-50)
			if(objSP.x-2==0) 
				return null;
		
		
		//if(objN.equals("RHS")||objN.equals("plr")) 
		if(obj>0 && obj<50)
			if(objSP.y-1==0) 
				return null;
			
			

		//if(objN.equals("RHL"))
		if(obj>=50)
			if(objSP.y-2==0) 
				return null;
			
		
		//move function
		//switch(objDes[0]) {
		//case "RVS":
		if(obj<-2 && obj> -50) {
			if( newGrid[objSP.x-1-1][objSP.y]==0) {
			        newGrid[objSP.x-1-1][objSP.y]=obj;
		            newGrid[objSP.x][objSP.y]=0;
			}else {return null;}
		}//case "RVL":
		else if(obj<-50) {
			if( newGrid[objSP.x-2-1][objSP.y]==0) {
				newGrid[objSP.x-2-1][objSP.y]=obj;
			
        newGrid[objSP.x][objSP.y]=0;
			}else {return null;}
		}//case "RHS":
		else if(obj>1 && obj<50) {
			if( newGrid[objSP.x][objSP.y-1-1]==0) {
			newGrid[objSP.x][objSP.y-1-1]=obj;
        newGrid[objSP.x][objSP.y]=0;	}else {return null;}
		}//case "RHL":
		else if(obj>50) {
		if( newGrid[objSP.x][objSP.y-2-1]==0) {
			newGrid[objSP.x][objSP.y-2-1]=obj;
        newGrid[objSP.x][objSP.y]=0;}else {return null;}
		}//case "plr":
		else if(obj==1) {
		if( newGrid[objSP.x][objSP.y-1-1]==0) {
			newGrid[objSP.x][objSP.y-1-1]=obj;
        newGrid[objSP.x][objSP.y]=0;
        pY-=1;}else {return null;}
		}
		
		
		
		//stone location update
		for(Stone st: stones) {
			
			if(st.id==obj) {
				
				if(st.vertical) {
					st.x-=1;}
				else {
					st.y-=1;
				}
			
				
			}
		}
		StoneAgeRunnerState newState = new StoneAgeRunnerState(newGrid,pX,pY,stones);
		return newState;
		
	}
	public Point locateObj(byte obj, StoneAgeRunnerState s) {
		Stone [] stones = s.stones;
		
		//String [] objDes = obj.split(" ");
		//if player 1
		if(obj == 1 )
			return new Point(s.runnerX,s.runnerY);
		
		for(int i =0 ;i<stones.length;i++) {
			if(stones[i].id==obj)
				return new Point(stones[i].x,stones[i].y);
		}
		return null;
	
	}

	protected int pathCost(String op) {
		// TODO Auto-generated method stub
		if(op.equals("moveUp,plr,1"))
			return 0;
		
		return 2;
	}


	public void genStateSpace(StoneAgeRunnerState currentState) {
		this.stateSpace = new HashMap<String, State>();
		StoneAgeProblem p = new StoneAgeProblem();
		//move up player id =1
		StoneAgeRunnerState s = p.moveUp((byte) 1, currentState);
		if(s!=null)
			stateSpace.put("moveUp,plr,1", s);
		// move down player id =1 
		 s=p.moveDown((byte)1, currentState);
		if(s!=null)
			stateSpace.put("moveDown,plr,1", s);
		
		for(int i=0;i<currentState.stones.length;i++) {
			s=null;
			
			s = p.moveUp(currentState.stones[i].id, currentState);
			if(s!=null)
				stateSpace.put("moveUp,"+currentState.stones[i].toString()+","+currentState.stones[i].id, s);
		}
		for(int i=0;i<currentState.stones.length;i++) {
			s=null;
			 s = p.moveDown(currentState.stones[i].id, currentState);
			if(s!=null)
				stateSpace.put("moveDown,"+currentState.stones[i].toString()+","+currentState.stones[i].id, s);
			
		}
	
		
		
	}


	@Override
	protected void genStateSpace(State s) {
		 this.genStateSpace((StoneAgeRunnerState)s);
		
	}
	
	
}
