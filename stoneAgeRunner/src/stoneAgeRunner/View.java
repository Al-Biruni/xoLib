package stoneAgeRunner;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class View extends JFrame{
	
	JPanel srP;
	private String[][] grid;
	public View(String[][] grid) {
		super("StoneAgeRunner");
		srP = new JPanel();
		this.setGrid(grid);
		// GridLayout experimentLayout = new GridLayout(0,2);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		srP
				.setMaximumSize(new Dimension(grid.length, grid[0].length));
		srP.setLayout(new GridLayout(grid.length, grid[0].length));
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				JButton t = new JButton(grid[i][j]);
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
				JButton t = new JButton(getGrid()[i][j]);
				srP.add(t);
			}
			
		}
		super.repaint();
		srP.revalidate();
		srP.repaint();
		
		
	}

	String[][] getGrid() {
		return grid;
	}

	void setGrid(String[][] grid) {
		this.grid = grid;
	}
}
