package stoneAgeRunner;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class View extends JFrame{
	
	JPanel srP;
	private byte[][] grid;
	public View(byte[][] state) {
		super("StoneAgeRunner");
		srP = new JPanel();
		this.setGrid(state);
		// GridLayout experimentLayout = new GridLayout(0,2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		srP
				.setMaximumSize(new Dimension(state.length, state[0].length));
		srP.setLayout(new GridLayout(state.length, state[0].length));
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state[0].length; j++) {
				JButton t=null;
				if(state[i][j]==0) {
				 t = new JButton("E");
				
				}else if(state[i][j]==1) {
					t = new JButton("plr");
				}else if(state[i][j]>1 && state[i][j]<50) {
					t = new JButton("RHS,"+state[i][j]);
				}else if(state[i][j]>=50) {
					t = new JButton("RHL,"+state[i][j]);
				}else if(state[i][j]<-1 && state[i][j]>-50) {
					t = new JButton("RVS,"+state[i][j]);
				}else if(state[i][j]<=-50) {
					t = new JButton("RVL,"+state[i][j]);
				}
					
					srP.add(t);
			}
}
		this.setContentPane(srP);

		this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void repaint() {
		
		srP.removeAll();
		//System.out.println("repainting");
		for (int i = 0; i < getGrid().length; i++) {
			for (int j = 0; j < getGrid()[0].length; j++) {
				JButton t=null;
				if(getGrid()[i][j]==0) {
				 t = new JButton("E");
				
				}else if(getGrid()[i][j]==1) {
					t = new JButton("plr");
				}else if(getGrid()[i][j]>1 && getGrid()[i][j]<50) {
					t = new JButton("RHS,"+getGrid()[i][j]);
				}else if(getGrid()[i][j]>=50) {
					t = new JButton("RHL,"+getGrid()[i][j]);
				}else if(getGrid()[i][j]<-1 && getGrid()[i][j]>-50) {
					t = new JButton("RVS,"+getGrid()[i][j]);
				}else if(getGrid()[i][j]<=-50) {
					t = new JButton("RVL,"+getGrid()[i][j]);
				}
					
					srP.add(t);
			}
			
		}
		super.repaint();
		srP.revalidate();
		srP.repaint();
		
		
	}

	byte[][] getGrid() {
		return grid;
	}

	void setGrid(byte[][] state) {
		this.grid = state;
	}
}
