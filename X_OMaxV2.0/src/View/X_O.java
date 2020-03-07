package View;

import Client.*;
import Commons.Exceptions.MessageCouldnotBeEncryptedException;
import Commons.Message.Message;
import Commons.User;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class X_O extends JFrame implements ListSelectionListener {
	public JRadioButton rdbtnPrivate ;
	public JRadioButton rdbtnPublic;
	public JTextPane msgArea;
	public JTextField msgText;
	public JTextField usrNameText;
	public JButton btnSend;
	public JButton checkUsrBtn;
	private ClientListener cl;
	public JDialog userReg;
	public JList onUsr ;
	public JList list;
    public DefaultListModel<User> listModel;
    JScrollPane listScrollPane;
	JFrame chatView = new JFrame();

	public X_O(ClientListener cl)  {
		super("View.X_O");
		
		this.cl =  cl;
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	
	}
	
	
	public void regDialog() {
		 userReg = new JDialog(this,"Choose a UserName");
		JPanel dialogPanel = new JPanel();
		
		
		userReg.setSize(200,200);
		usrNameText = new JTextField();
		usrNameText.setText("Enter userName");
		usrNameText.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				usrNameText.setText("");
			}
		}); 
		
		dialogPanel.add(usrNameText);
		
		 checkUsrBtn = new JButton("select");
	
		 
		 checkUsrBtn.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent arg0) {
				 cl.Register(usrNameText.getText());
			 }
			 
		 });
		
		 dialogPanel.add(checkUsrBtn);
		 userReg.add(dialogPanel);
userReg.setVisible(true);
		
		
	}
	
	

	public void chatView() {
        //Container chatCon = new Container();

        JPanel msgControls = new JPanel(new GridBagLayout()); //message control 
        
		JPanel msgsAndUsers = new JPanel(new GridBagLayout());//messages and users
		
		GridBagLayout mainLayout = new GridBagLayout();
	this.setLayout(mainLayout);
		//---------------------------------------------------------------------------------------
		//GroupLayout gLayout = new GroupLayout(chatCon);
		
		
		
		this.setLayout(mainLayout);
		
		
		
		
		
		 
		 
		
		
		//controls
		 
		 
		    msgText = new JTextField();
		    msgText.setText("Enter Commons.Message.Message.Commons.Message.Message");
		    
			
			GridBagConstraints gbc_msgText = new GridBagConstraints();
			gbc_msgText.weighty=1;
			gbc_msgText.ipadx=400;
			gbc_msgText.ipady=30;
			gbc_msgText.gridx = 0;
			gbc_msgText.gridy = 0;
			gbc_msgText.fill=GridBagConstraints.HORIZONTAL;
			
			msgControls.add(msgText, gbc_msgText);
			
			
			GridBagConstraints gbc_btnSend = new GridBagConstraints();
			gbc_btnSend.gridx = 15;
			gbc_btnSend.gridy = 0;
			
		 
		btnSend = new JButton("send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					getCl().onSend();
				} catch (MessageCouldnotBeEncryptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		//msgCnt.add(btnSend, gbc_btnSend);
		msgControls.add(btnSend, gbc_btnSend);
		 rdbtnPublic = new JRadioButton("public");
		 rdbtnPublic.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) {
				 rdbtnPrivate.setSelected(false);
				  list.setSelectedIndex(-1);
				 updateList();
			 }

			
		 });
		 
		GridBagConstraints gbc_rdbtnPublic = new GridBagConstraints();
		gbc_rdbtnPublic.gridx = 14;
		gbc_rdbtnPublic.gridy = 1;
		//msgCnt.add(rdbtnPublic, gbc_rdbtnPublic);
		msgControls.add(rdbtnPublic, gbc_rdbtnPublic);
		
		 rdbtnPrivate = new JRadioButton("private");
		 rdbtnPrivate.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent arg0) {
				 rdbtnPublic.setSelected(false);
				  list.setSelectedIndex(0);
			 }

			
		 });
		GridBagConstraints gbc_rdbtnPrivate = new GridBagConstraints();
		gbc_rdbtnPublic.anchor=GridBagConstraints.PAGE_START;
		gbc_rdbtnPrivate.gridx = 14;
		gbc_rdbtnPrivate.gridy = 0;
		msgControls.add(rdbtnPrivate, gbc_rdbtnPrivate);
		//msgCnt.add(rdbtnPrivate);
		
		
		
		msgArea = new JTextPane();
		msgArea.setEditable(false);
		msgArea.setText("Old messages");
		
		
		GridBagConstraints gbc_msgArea = new GridBagConstraints();
		gbc_msgArea.anchor = GridBagConstraints.FIRST_LINE_START;
		
		gbc_msgArea.fill=GridBagConstraints.BOTH;
		gbc_msgArea.ipadx=400;
		gbc_msgArea.ipady=400;
		gbc_msgArea.gridx = 0;
		gbc_msgArea.gridy = 0;
		// msgUs.add(msgArea, gbc_msgArea);
		 msgsAndUsers.add(msgArea, gbc_msgArea);
		
		
		 onUsr = new JList();
		 
		 listModel = new DefaultListModel<User>();
	       

	        //Create the list and put it in a scroll pane.
	        list = new JList(listModel);
	        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        list.setSelectedIndex(0);
	        list.addListSelectionListener(this);
	        list.setVisibleRowCount(5);
	      listScrollPane = new JScrollPane(list);
		
		GridBagConstraints gbc_onUsr = new GridBagConstraints();
		
		gbc_onUsr.insets = new Insets(0, 10,0, 0);
		gbc_onUsr.ipadx=25;
		gbc_onUsr.ipady=300;
		gbc_onUsr .gridx = 12;
		gbc_onUsr .gridy = 0;
		
		 msgsAndUsers.add(listScrollPane,gbc_onUsr);
		
		
		GridBagConstraints gbc_main = new GridBagConstraints();
		//gbc_main.anchor = GridBagConstraints.FIRST_LINE_START;
		gbc_main.insets = new Insets(0, 0, 10, 0);
	    //gbc_main.ipadx=300;
	  //  gbc_main.ipady=300;
		gbc_main.gridx = 0;
		gbc_main.gridy = 0;
		//gbc_main.fill= GridBagConstraints.BOTH;
		this.add(msgsAndUsers, gbc_main);
		
		
		//gbc_main.anchor = GridBagConstraints.LAST_LINE_START;
		//gbc_main.insets = new Insets(0, 0, 0, 0);
		//gbc_main.fill= GridBagConstraints.VERTICAL;
		//gbc_main.ipadx=200;
	    //gbc_main.ipady=30;
		gbc_main.gridx = 0;
		gbc_main.gridy = 15;
		
		this.add(msgControls, gbc_main);
	

		 this.setSize(600,600);
		
		this.setVisible(true);
		
		
	}

	public void updateList() {
	     list = new JList(listModel);
	        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        list.setSelectedIndex(0);
	        list.addListSelectionListener(this);
	        list.setVisibleRowCount(5);
		listScrollPane = new JScrollPane(list);
		
	}
	
	
	

	ClientListener getCl() {
		return cl;
	}

	void setCl(ClientListener cl) {
		this.cl =  cl;
	}


	public JLabel msgToLabel(Message msg) {
		JLabel msgL = new JLabel();
		
		msgL.setText(msg.sender+" : "+ msg.msgBody);
		
		return msgL;
	}

	@Override
	public void valueChanged(ListSelectionEvent arg0) {
		this.rdbtnPrivate.setSelected(true);
		this.rdbtnPublic.setSelected(false);
		
	}
	public static void main(String [] args){
		Client user = new Client();
		ClientController userViewController = new ClientController(user);
		X_O win = new X_O(userViewController);
		userViewController.setView(win);
		win.regDialog();
		user.run();
		win.setVisible(true);


	}

}