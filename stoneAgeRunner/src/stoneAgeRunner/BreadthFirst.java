package stoneAgeRunner;

import java.util.Comparator;

public class BreadthFirst implements Comparator<Node>  {



	@Override
	public int compare(Node o1, Node o2) {
		// TODO Auto-generated method stub
	return o1.depth-o2.depth;
	}

}
