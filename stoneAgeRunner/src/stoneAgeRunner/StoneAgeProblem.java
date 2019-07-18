package stoneAgeRunner;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

public class StoneAgeProblem extends Problem {
	
	Random rand = new Random();
	
	
	public StoneAgeRunnerState genGrid() {
		
		//int n = rand.nextInt(6) + 4;
		//int m = rand.nextInt(6) + 4;
		int n = 9;
		int m = 9;
		double difficulty =0.80;
		String[][] grid = new String[n][m];
		int rY = rand.nextInt(m - 2) + 1;
		grid[1][rY]="plr";
		grid[1][rY-1]="plr";
		int pX =1;
		int pY = rY;
		
		int rockN = (int)(((m*n)/3)*difficulty);
		//System.out.println(rockN);
	Stone[] stones = new Stone[rockN];
		int i=0;
			while(i<rockN) {
				//System.out.println(i);
			int randrockPlacment = rand.nextInt(2);
			int randrockLen = rand.nextInt(2)+2;
			int rRx;
			int rRy;
			boolean aFlag = false;
			switch(randrockPlacment) {
			//horizontal case
			case 0 :
				while(!aFlag) {
				 rRx = rand.nextInt(n);
				 rRy = rand.nextInt(m-randrockLen)+randrockLen;
				 boolean f=true;
				 for(int c=0;c<randrockLen;c++) {
					if(grid[rRx][rRy-c]!=null||rRx==1) 
						f=false;
				 }
				 if(!f)
					 continue;
				 aFlag=true;
				 stones[i]= new Stone(rRx,rRy,i,false,randrockLen);
				
				 for(int c=0;c<randrockLen;c++) {
					 switch(randrockLen) {
					 case 2 :grid[rRx][rRy-c]="RHS "+i;break;
					 
					 case 3 :grid[rRx][rRy-c]="RHL "+i;break;
					 default:System.err.println("wrong car length : " + randrockLen);
					 }
					
					 } i++;	
				}break;
				//vertical case
			case 1:
				while(!aFlag) {
				 rRx = rand.nextInt(n-randrockLen)+randrockLen;
				 rRy = rand.nextInt(m);
				 boolean f =true;
				 for(int c=0;c<randrockLen;c++) {
						if(grid[rRx-c][rRy]!=null) 
							f=false;
				 }
				 if(!f)
					 continue;
					 aFlag=true;
					 stones[i]= new Stone(rRx,rRy,i,true,randrockLen);
					
					 for(int c=0;c<randrockLen;c++) {
						 switch(randrockLen) {
						 case 2 :grid[rRx-c][rRy]="RVS "+i;break;
						 
						 case 3 :grid[rRx-c][rRy]="RVL "+i;break;
						 default:System.err.println("wrong Stone length : " + randrockLen);
						 }
								
						 } i++;
					}break;
				default: System.err.println("random Stone placment is not 0 or 1 :" + randrockPlacment);
			}
		}
			for(int i1 =0;i1<grid.length;i1++)
				for(int j=0;j<grid[0].length;j++)
					if(grid[i1][j]==null)
						grid[i1][j]="E";
			
		StoneAgeRunnerState intialState = new StoneAgeRunnerState(grid,pX,pY,stones);
		System.out.println(intialState.toString());
		return intialState;
		
	}

	
	public StoneAgeRunnerState moveUp(String obj, StoneAgeRunnerState s){
		
		Point objSP = locateObj(obj,s);
		
		String[][]newGrid = new String [s.state.length][s.state[0].length];
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
		

		String[] objDes=obj.split(" ");
		String objN=objDes[0];
		
		//edge limiters
		if(objN.equals("RVS")||objN.equals("RVL"))
			if(objSP.x==(n-1)) 
				return null;
			
		
		if(objN.equals("RHS")||objN.equals("RHL")||objN.equals("plr"))
			if(objSP.y==(m-1)) 
				return null;
		
		
		//move function
		switch(objDes[0]) {
		case "RVS":
			if( newGrid[objSP.x+1][objSP.y]=="E") {
			        newGrid[objSP.x+1][objSP.y]="RVS "+ objDes[1];
		            newGrid[objSP.x-1][objSP.y]="E";
			}else {return null;}
		
			break;
		case "RVL":
			if( newGrid[objSP.x+1][objSP.y]=="E") {
				newGrid[objSP.x+1][objSP.y]="RVL "+ objDes[1];
			
        newGrid[objSP.x-2][objSP.y]="E";
			}else {return null;}
		break;
		
		case "RHS":
			if( newGrid[objSP.x][objSP.y+1]=="E") {
			newGrid[objSP.x][objSP.y+1]="RHS "+ objDes[1];
        newGrid[objSP.x][objSP.y-1]="E";	}else {return null;}
			break;
			
		case "RHL":if( newGrid[objSP.x][objSP.y+1]=="E") {
			newGrid[objSP.x][objSP.y+1]="RHL "+ objDes[1];
        newGrid[objSP.x][objSP.y-2]="E";}else {return null;}
			break;
			
		case "plr":if( newGrid[objSP.x][objSP.y+1]=="E") {
			newGrid[objSP.x][objSP.y+1]="plr";
        newGrid[objSP.x][objSP.y-1]="E";
        pY+=1;}else {return null;}
			break;
		
		}
		
		//stone location update
		for(Stone st: stones) {
			
			if(st.toString().equals(obj)) {
				
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
	
	public  StoneAgeRunnerState moveDown(String obj, StoneAgeRunnerState s){
		
		Point objSP = locateObj(obj,s);
		
		String[][]newGrid = new String [s.state.length][s.state[0].length];
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
		

		String[] objDes=obj.split(" ");
		String objN=objDes[0];
		
		//edge limiters
		if(objN.equals("RVS"))
			if(objSP.x-1==0) 		
				return null;
			
		if(objN.equals("RVL")) 
			if(objSP.x-2==0) 
				return null;
		
		
		if(objN.equals("RHS")||objN.equals("plr")) 
			if(objSP.y-1==0) 
				return null;
			
			

		if(objN.equals("RHL"))
			if(objSP.y-2==0) 
				return null;
			
		
		//move function
		switch(objDes[0]) {
		case "RVS":
			if( newGrid[objSP.x-1-1][objSP.y]=="E") {
			        newGrid[objSP.x-1-1][objSP.y]="RVS "+ objDes[1];
		            newGrid[objSP.x][objSP.y]="E";
			}else {return null;}
		
			break;
		case "RVL":
			if( newGrid[objSP.x-2-1][objSP.y]=="E") {
				newGrid[objSP.x-2-1][objSP.y]="RVL "+ objDes[1];
			
        newGrid[objSP.x][objSP.y]="E";
			}else {return null;}
		break;
		
		case "RHS":
			if( newGrid[objSP.x][objSP.y-1-1]=="E") {
			newGrid[objSP.x][objSP.y-1-1]="RHS "+ objDes[1];
        newGrid[objSP.x][objSP.y]="E";	}else {return null;}
			break;
			
		case "RHL":if( newGrid[objSP.x][objSP.y-2-1]=="E") {
			newGrid[objSP.x][objSP.y-2-1]="RHL "+ objDes[1];
        newGrid[objSP.x][objSP.y]="E";}else {return null;}
			break;
			
		case "plr":if( newGrid[objSP.x][objSP.y-1-1]=="E") {
			newGrid[objSP.x][objSP.y-1-1]="plr";
        newGrid[objSP.x][objSP.y]="E";
        pY-=1;}else {return null;}
			break;
		
		}
		
		//stone location update
		for(Stone st: stones) {
			
			if(st.toString().equals(obj)) {
				
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
	public Point locateObj(String obj, StoneAgeRunnerState s) {
		Stone [] stones = s.stones;
		
		String [] objDes = obj.split(" ");
		if(objDes[0].equals("plr"))
			return new Point(s.runnerX,s.runnerY);
		
		for(int i =0 ;i<stones.length;i++) {
			if(stones[i].id==Integer.parseInt(objDes[1]))
				return new Point(stones[i].x,stones[i].y);
		}
		return null;
	
	}

	protected int pathCost(String op) {
		// TODO Auto-generated method stub
		if(op.equals("moveUp,plr"))
			return 0;
		
		return 2;
	}


	
	
	
}
