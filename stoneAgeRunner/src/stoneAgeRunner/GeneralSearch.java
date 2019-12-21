package stoneAgeRunner;


import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.TimeUnit;



public  class GeneralSearch {
	Problem p;
	Queue<Node> que ;
	String queF="";
	ArrayList<String>  seenStates = new ArrayList<String>(); 
	
	
	//static int nEx=0;
	int qSize=0;
	int aDepth=1;
	
	public GeneralSearch(Problem p , String QingFun){
		this.p=p;
		this.queF=QingFun;
		
		//create initial node 
		Node root = new Node(p.intialState);
		//define Qing function
		switch(QingFun){
		case"BF" : que = new PriorityQueue<Node>(50000000,new BreadthFirst());break;
		case "DF" :  que = new PriorityQueue<Node>(50000000,new DepthFirst());break;
		case "UC" :  que = new PriorityQueue<Node>(5000000,new UniformCost());break;
		case "ID" :que = new PriorityQueue<Node>(5000000,new DepthFirst());break;
		case"G1": que = new PriorityQueue<Node>(5000000,new GreedySearch(1));break;
		case"G2":que = new PriorityQueue<Node>(50000000,new GreedySearch(2));break;
		case"AS1":que = new PriorityQueue<Node>(5000000,new AStarSearch(1));break;
		case"AS2":que = new PriorityQueue<Node>(50000000,new AStarSearch(2));break;
		}
		//generate root children
		genChildren(root);
		
		//System.out.println(solve());

		
	}
	
	
	
public String solve() {
	String solution="";
	int i =0;
	int mE =0;
		while(true){
			if(que.isEmpty()) {
				if(queF.equals("ID")&&aDepth<15) {
					aDepth++;
					seenStates.clear();
					Node root = new Node(p.intialState);
					genChildren(root);
				return	this.solve();
				
				}
				System.out.println("NUmber of nodes explored : " + i);
				return "No solution";
			}
			Node cN = que.poll();
			
			if(cN.myState.compareTo(p.goalState)==0){
				System.out.println("found goal");
				Node t = cN.parent;
				if(t!=null)
				solution+= cN.operator+"\n";
				
				while(t.parent!=null)
				{
				solution+=t.operator+"\n";
				t=t.parent;
				}
				System.out.println("NUmber of nodes tested : " + i);
				System.out.println("number of nodes that generated children "+ mE);
				System.out.println("NUmber of childeren added to q  : " + qSize);
				return solution;
				
			}else{
				
				String hash = cN.myState.hash();
			//	System.out.println(cN.myState.toString());
			//	System.out.println(hash);
				if(!seenStates.contains(hash)) {
					 genChildren(cN);
					 mE++;
					// System.out.println("generated childern");
					 seenStates.add(hash);
				}
				
			
			}
			
			i++;	
		}
		
	}



private String neg(String operator) {
	String [] split = operator.split(",");
	
	switch(split[0]){
	case "moveUp": return "moveDown,"+ split[1];
	case "moveDown": return "moveUp,"+ split[1];
	
	}
	return null;
}



private void genChildren(Node N) {
	//System.out.println(N.operator+ "D "+ N.depth);
	N.myState.genStateSpace();;
	
		
	for(String op : N.myState.nextStates.keySet()){
		int cost = N.pathCost + p.pathCost(op);
		Node child = new Node(N.myState.nextStates.get(op),N,op,N.depth+1,cost);
		if(queF.equals("ID")) {
			
			if(child.depth<aDepth)
		que.add(child);
			
		}else {
			//cycle control
			//if(N.operator!=null) {
			//String negOp = neg(child.operator);

			//if(!negSeenStates.contains(cN.operator)) 
		      //  negSeenStates.add(negOp);
			//if(!N.operator.equals(negOp)) {
			que.add(child);
			qSize++;//}
			//}else {
				//que.add(child);
			//}
			
		}
		
		//System.out.println(child.operator + " D child " + child.depth);
		
	}
		
		
	}


}
