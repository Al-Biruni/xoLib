import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;


public class X_O extends JFrame {
	public JRadioButton rdbtnPrivate ;
	public JRadioButton rdbtnPublic;
	public JTextPane txtpnMessagearea;
	public JTextField txtEnterMessage;
	public JButton btnSend;
	private ClientListener cl;

	public X_O()  {
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			getCl().onSend();
			}
		});
		
		 rdbtnPublic = new JRadioButton("public");
		GridBagConstraints gbc_rdbtnPublic = new GridBagConstraints();
		gbc_rdbtnPublic.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPublic.gridx = 11;
		gbc_rdbtnPublic.gridy = 2;
		getContentPane().add(rdbtnPublic, gbc_rdbtnPublic);
		
		 rdbtnPrivate = new JRadioButton("private");
		GridBagConstraints gbc_rdbtnPrivate = new GridBagConstraints();
		gbc_rdbtnPrivate.insets = new Insets(0, 0, 5, 5);
		gbc_rdbtnPrivate.gridx = 11;
		gbc_rdbtnPrivate.gridy = 3;
		getContentPane().add(rdbtnPrivate, gbc_rdbtnPrivate);
		
		 txtpnMessagearea = new JTextPane();
		txtpnMessagearea.setEditable(false);
		txtpnMessagearea.setText("");
		GridBagConstraints gbc_txtpnMessagearea = new GridBagConstraints();
		gbc_txtpnMessagearea.gridheight = 5;
		gbc_txtpnMessagearea.gridwidth = 10;
		gbc_txtpnMessagearea.insets = new Insets(0, 0, 5, 5);
		gbc_txtpnMessagearea.fill = GridBagConstraints.BOTH;
		gbc_txtpnMessagearea.gridx = 1;
		gbc_txtpnMessagearea.gridy = 1;
		getContentPane().add(txtpnMessagearea, gbc_txtpnMessagearea);
		
		txtEnterMessage = new JTextField();
		GridBagConstraints gbc_txtEnterMessage = new GridBagConstraints();
		gbc_txtEnterMessage.gridwidth = 10;
		gbc_txtEnterMessage.insets = new Insets(0, 0, 5, 5);
		gbc_txtEnterMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtEnterMessage.gridx = 1;
		gbc_txtEnterMessage.gridy = 7;
		getContentPane().add(txtEnterMessage, gbc_txtEnterMessage);
		txtEnterMessage.setColumns(10);
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.anchor = GridBagConstraints.NORTH;
		gbc_btnSend.insets = new Insets(0, 0, 5, 5);
		gbc_btnSend.gridx = 11;
		gbc_btnSend.gridy = 7;
		getContentPane().add(btnSend, gbc_btnSend);
		
	
	}

	ClientListener getCl() {
		return cl;
	}

	void setCl(ClientListener cl) {
		this.cl = cl;
	}




}
