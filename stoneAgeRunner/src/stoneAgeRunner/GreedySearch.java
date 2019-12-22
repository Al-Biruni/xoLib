package stoneAgeRunner;

import java.util.Comparator;

import stoneAgeRunner.Node;

public class GreedySearch implements Comparator< Node> {
	Boolean fun1 ,fun2;

	public GreedySearch(int i) {
		switch(i){
		case 1: fun1=true;fun2=false;break;
		case 2 : fun1=false;fun2=true;break;
		}
	}

	@Override
	public int compare(Node n1, Node n2) {
		//the first greedy function always choose to move the agent first else breadth first 
		//what if no move up next block is not empty 
		if(fun1){
			if(n1.operator.equals("moveUp,plr,1"))
					return -1;
			
			
			//if(n2.operator.equals("MoveUp 0"))
				//return 1;
			else
				return n1.depth-n2.depth;
		}
		
		
		if(fun2){
			
			if(n1.operator.equals("moveUp,plr,1"))
				return -5;
			
			String sB;
			StoneAgeRunnerState t = ((StoneAgeRunnerState)n1.myState);
			 sB = t.getblockingStone();
	
			
		
			 if(n1.operator.contains("moveUp,"+sB)) {
					//System.out.println(n1.operator);
				 return -5;
					 }
		
			
	
			return n1.depth-n2.depth;
			}
			
			
			
		
		
		
		
		
		
		
		
		
		// TODO Auto-generated method stub
		return 0;
	}

}
