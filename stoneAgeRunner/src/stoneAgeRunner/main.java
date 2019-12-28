package stoneAgeRunner;

import java.util.concurrent.TimeUnit;


public class main {
	
	public static void main(String [] args) {
		StoneAgeProblem p= new StoneAgeProblem();
		p.intialState = p.genGrid();
		StoneAgeRunnerState sAs= (StoneAgeRunnerState) p.intialState;
		
		//set true to turn on GUI
		Boolean visable=true; 
		View v = null ;
		if(visable)
		v = new View(p.intialState.state);
		
		//p.moveUp("plr", (StoneAgeRunnerState)p.intialState);
		//p.printGrid(p.intialState.state);
		
		/*CHOOSE SEARCH METHOD
		 * DF:depthFirst    BF:breadthFirst
		 * ID:iterativeDepth  UC:uniformCost
		 * G1                G2
		 * AS1				As2
		 */
		
		String searchMethod = "AS2";
		 
		GeneralSearch gs = new GeneralSearch(p,searchMethod);
		System.out.println("Search using " + searchMethod);
		
		System.out.println("_____________________");
		String solution = gs.solve();
		//System.out.println(solution);
		String [] operations = solution.split("\n");
		String [] opRev = new String[operations.length];
		for(int ri =0 ; ri<opRev.length;ri++){
			opRev[ri]=operations[operations.length-1-ri];
			String [] op = opRev[ri].split(","); 
			System.out.println(opRev[ri]);
			if(visable) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch(op[0]) {
				case "moveUp":
				 sAs = p.moveUp((byte)Integer.parseInt(op[2]), sAs);
				v.setGrid(sAs.state);
				v.repaint();break;
				
				case "moveDown":
					sAs=p.moveDown((byte)Integer.parseInt(op[2]),sAs);
					v.setGrid(sAs.state);
					v.repaint();break;
				
				}
				
			}
			
}
		
	}
}
