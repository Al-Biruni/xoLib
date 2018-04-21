

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextPane;

public class Dukress extends JFrame {

	public JButton b1;
	public JButton b2;
	public JButton b3;
	public JButton b4;
	public JButton b5;
	public JButton b6;
	public JButton b7;
	public JButton b8;
	public JButton b9;
	public JButton b10;

	public JTextPane resArea;
	
	Browser b;
	
	public Dukress(){
		resArea = new JTextPane();
	b = new Browser(resArea);
	
	GridBagLayout gridBagLayout = new GridBagLayout();
	getContentPane().setLayout(gridBagLayout);
	
	this.setVisible(true);
	this.setSize(400, 600);
	GridBagConstraints gbc_resArea = new GridBagConstraints();
	
	gbc_resArea.gridx = 2;
	gbc_resArea.gridy = 5;
	gbc_resArea.gridheight=4;
	gbc_resArea.gridwidth=4;
	resArea.setSize(200, 200);
	getContentPane().add(resArea,gbc_resArea);
	
GridBagConstraints gbc_b = new GridBagConstraints();
	
	gbc_b.gridx = 5;
	gbc_b.gridy = 1;

	//Dukress/src
	
	b1 = new JButton("get test.png ");
	b1.addActionListener(new ActionListener(){
		

		@Override
		public void actionPerformed(ActionEvent e) {
			Request r = new Request("GET src/RequestHandler/docroot/test.png");
			r.addHeader("Dukress");
			r.addHeader("png");
			r.addHeader("keep-alive");
			System.out.println(r.toString());
            b.sendRequest(r);
			
		}
	});
	
	
	
	b2 = new JButton("get Red-Book.jpg ");
	b2.addActionListener(new ActionListener(){
		

		@Override
		public void actionPerformed(ActionEvent e) {
			Request r = new Request("GET src/RequestHandler/docroot/Rek.jpg");
			r.addHeader("Dukress");
			r.addHeader("jpg");
			r.addHeader("keep-alive");
			System.out.println(r.toString());
            b.sendRequest(r);
			
		}
	});
	
	b3 = new JButton("get parser.txt ");
	b3.addActionListener(new ActionListener(){
		

		@Override
		public void actionPerformed(ActionEvent e) {
			Request r = new Request("GET src/RequestHandler/docroot/parser.txt");
			r.addHeader("Dukress");
			r.addHeader("txt");
			r.addHeader("keep-alive");
			System.out.println(r.toString());
            b.sendRequest(r);
			
		}
	});
	
	
	
	
	
	
	
	
	
	
	
	getContentPane().add(b1,gbc_b);
	getContentPane().add(b2);
	getContentPane().add(b3);
	b.start();
	}
	
	
	
	public static void main (String [] args){
		Dukress d = new Dukress();
	}
	
	
	
	
	
	
	
}
