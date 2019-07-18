package stoneAgeRunner;

import java.util.Comparator;

import stoneAgeRunner.Node;

public class AStarSearch implements Comparator<Node> {
	Boolean fun1 ,fun2;
	public AStarSearch(int i) {
		switch(i){
		case 1: fun1=true;fun2=false;break;
		case 2 : fun1=false;fun2=true;break;
		}
	}
	@Override
	public int compare(Node o1, Node o2) {
	//distance between player and exit
		if(fun1){
			
		int o1p =o1.pathCost,o2p =o2.pathCost;
		
		//System.out.println(o1p);
		o1p+=((StoneAgeRunnerState)o1.myState).estimateGoal(fun1);
		o2p+=((StoneAgeRunnerState)o1.myState).estimateGoal(fun1);
		//System.out.println(o1p);
		
		//lower cost higher priority
		return o1p-o2p;
		}
		
		
		
		//the more empty cells in the second row the better the node 
		if(fun2){
			
			int o1p =o1.pathCost,o2p =o2.pathCost;
			o1p+=((StoneAgeRunnerState)o1.myState).estimateGoal(fun1);
			o2p+=((StoneAgeRunnerState)o1.myState).estimateGoal(fun1);
			
				
				return o1p-o2p;
		}
		return 0;
	}

}
