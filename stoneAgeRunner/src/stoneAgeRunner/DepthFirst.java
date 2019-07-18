package stoneAgeRunner;

import java.util.Comparator;

public class DepthFirst implements Comparator<Node>  {


	
	

	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
	return o2.depth-o1.depth;
	}

}