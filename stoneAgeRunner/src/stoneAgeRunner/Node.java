package stoneAgeRunner;


public class Node {
	State myState;
	Node parent;
	String operator;
	int depth;
	int pathCost;
	//root
	public Node(State is ){
		this.myState=is;
		operator="";
		parent =null;
		depth=0;
		pathCost=0;
	}
	public Node(State s ,Node p, String op , int d , int pc){
		this.myState=s;
		this.parent=p;
		this.operator=op;
		this.depth=d;
		this.pathCost=pc;
		
	}
	

}
