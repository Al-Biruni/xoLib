package secProj;

import java.io.IOException;

public class Simulator {
	static Node userSimulator = new Node();
	
	public static void main(String[]args){
		Node a = new Node();
		
		Node b = new Node();
		Node c = new Node();
		Node d = new Node();
		Node e = new Node();
		Node f = new Node();
		Node g = new Node();
		Node h = new Node();
		Node i = new Node();
		Node j = new Node();
		
		
			
			Transaction t = new Transaction();
	
			// TODO Auto-generated catch block
		
	userSimulator.connectToNode(a);
	userSimulator.connectToNode(b);
	userSimulator.connectToNode(c);
	userSimulator.connectToNode(d);
	userSimulator.connectToNode(e);
	userSimulator.connectToNode(f);
	userSimulator.connectToNode(g);
	userSimulator.connectToNode(h);
	userSimulator.connectToNode(i);
	
	userSimulator.annonceTransaction(t);
	
	
		
		
	}

}
