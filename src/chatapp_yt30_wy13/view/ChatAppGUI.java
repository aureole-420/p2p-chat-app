package chatapp_yt30_wy13.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import chatapp_yt30_wy13.impl.ProxyUser;
import common.IChatRoom;
import common.IUser;

import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;

/**
 * main view for rmi chat app
 * @author yt30, wy13.
 */
public class ChatAppGUI extends JFrame {

	/**
	 * SerialUID
	 */
	private static final long serialVersionUID = 1066753910053362594L;
	/**
	 * content/main panel.
	 */
	private JPanel contentPane;
	/**
	 * control panel.
	 */
	private final JPanel _pnlCtrl = new JPanel();
	/**
	 * subpanel showing ports.
	 */
	private final JPanel _pnlPort = new JPanel();
	/**
	 * label notifying server port.
	 */
	private final JLabel _lblServerPort = new JLabel("Server Port");
	/**
	 * label notifying user port.
	 */
	private final JLabel _lblBoundPort = new JLabel("UserStub Port");
	/**
	 * txt field for server port.
	 */
	private final JTextField _tfServerPort = new JTextField();
	/**
	 * txtfield for user port.
	 */
	private final JTextField _tfBoundPort = new JTextField();
	
	/**
	 * subpanel for login
	 */
	private final JPanel _pnlLogIn = new JPanel();
	/**
	 * label notifying username
	 */
	private final JLabel _lblUsername = new JLabel("Username");
	/**
	 * label notifying local ipaddress
	 */
	private final JLabel _lblServername = new JLabel("Local IP");
	/**
	 * text field for user name
	 */
	private final JTextField _tfUsername = new JTextField();
	/**
	 * text field for server name
	 */
	private final JTextField _tfServername = new JTextField();
	/**
	 * panel for user name.
	 */
	private final JPanel _pnlUsername = new JPanel();
	/**
	 * subpanel for servername.
	 */
	private final JPanel _pnlServername = new JPanel();
	/**
	 * click the button to register the user.
	 */
	private final JButton _btnLogIn = new JButton("Log in");
	
	/**
	 * subpanel for creating new chatroom
	 */
	private final JPanel _pnlNewChatRoom = new JPanel();
	/**
	 * textfield for new chatroom.
	 */
	private final JTextField _tfChatroomName = new JTextField();
	/**
	 * click the button to create a chatroom
	 */
	private final JButton _btnCreateChatRoom = new JButton("Create");
	/**
	 * subpanel for connecting to.
	 */
	private final JPanel _pnlConnectTo = new JPanel();
	/**
	 * text field for entering remote ipaddress.
	 */
	private final JTextField _tfRemoteIP = new JTextField();
	/**
	 * click the button to connect to the remote host.
	 */
	private final JButton _btnConnectTo = new JButton("Connect");
	/**
	 * subpanel for connected host.
	 */
	private final JPanel _pnlConnectedHost = new JPanel();
	/**
	 * drop list showing connected host/user.
	 */
	private final JComboBox<IUser> _cbConnectedHost = new JComboBox<IUser>();
	/**
	 * click the button to request to join one of remote host's chatrooms
	 */
	private final JButton _btnRequest = new JButton("Request");
	/**
	 * click the button to log out.
	 */
	private final JButton _btnLogOut = new JButton("Log out");
	
	//private final JButton _btnTest = new JButton("File Chooser");
	/**
	 * The tabbed panel showing chatrooms.
	 */
	private final JTabbedPane _tpDisplay = new JTabbedPane(JTabbedPane.TOP);
	/**
	 * The panel for informations.
	 */
	private final JPanel _pnlInfo = new JPanel();
	/**
	 * scroll panel to hold tabbed panel.
	 */
	private final JScrollPane _spInfo = new JScrollPane();
	/**
	 * TheText area showing information.
	 */
	private final JTextArea _taInfo = new JTextArea();
	/**
	 * subpanel for mode setting.
	 */
	private final JPanel _pnlMode = new JPanel();
	/**
	 * click the button to set to normal mode.
	 */
	private final JButton _btnNormal = new JButton("Testing User 1");
	/**
	 * click the button to set to testing mode.
	 */
	private final JButton _btnTest = new JButton("Testing User 2");
	
	/**
	 * Adapter that allows view to call main model's method.
	 */
	private IView2ModelAdapter _mainModel;
	/**
	 * The list of connected users.
	 */
	private DefaultComboBoxModel<IUser> friendsList = new DefaultComboBoxModel<IUser>();

	/**
	 * Create the frame.
	 */
	public ChatAppGUI(IView2ModelAdapter mainModel) {
		this._mainModel = mainModel;
		initGUI();
	}
	
	/**
	 * Initialize the GUI.
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);	
		_pnlCtrl.setBorder(new TitledBorder(null, "Control Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		contentPane.add(_pnlCtrl, BorderLayout.NORTH);
		_pnlMode.setBorder(new TitledBorder(null, "Working Mode", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		_pnlCtrl.add(_pnlMode);
		_pnlMode.setLayout(new BoxLayout(_pnlMode, BoxLayout.Y_AXIS));
		_btnNormal.setToolTipText("DONT'S CLICK THIS UNLESS YOU ARE GOING TO START TWO CHATAPP USING A SINGLE IP!!! ");
		_pnlMode.add(_btnNormal);
		_btnTest.setToolTipText("DONT'S CLICK THIS UNLESS YOU ARE GOING TO START TWO CHATAPP USING A SINGLE IP!!! ");
		_pnlMode.add(_btnTest);
		_btnNormal.addActionListener(new ActionListener() { // set normal mode
			public void actionPerformed(ActionEvent e) {
				_mainModel.setNormalMode();
				_btnTest.setEnabled(false);
				_btnNormal.setEnabled(false);
			}
		});
		
		_btnTest.addActionListener(new ActionListener() { // set test mode
			public void actionPerformed(ActionEvent e) {
				_mainModel.setTestMode();
				_btnTest.setEnabled(false);
				_btnNormal.setEnabled(false);
			}
		});
		
		_pnlPort.setBorder(new TitledBorder(null, "Testing Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		_pnlCtrl.add(_pnlPort);
		_pnlPort.setLayout(new GridLayout(2, 2, 0, 0));
		_tfServerPort.setToolTipText("THE SERVER PORT WILL BE DISPLAYED AFTER LOGGING IN.");
		_tfServerPort.setText("AUTO");
		
		_tfServerPort.setColumns(4);
		_tfBoundPort.setToolTipText("THE USER STUB PORT WILL BE DISPLAYED AFTER LOGIN.");
		_tfBoundPort.setText("AUTO");
		
		_tfBoundPort.setColumns(4);
		_pnlPort.add(_lblServerPort);
		_pnlPort.add(_tfServerPort);
		_pnlPort.add(_lblBoundPort);
		_pnlPort.add(_tfBoundPort);
		
		_pnlLogIn.setBorder(new TitledBorder(null, "Login Panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_pnlCtrl.add(_pnlLogIn);
		_pnlLogIn.setLayout(new GridLayout(3, 1, 0, 0));
		_tfUsername.setColumns(10);
		_tfServername.setToolTipText("THE LOCAL IP ADDRESS WILL BE DISPLAYED AUTOMATICALLY AFTER LOGGING IN");
		_tfServername.setText("AUTO");
		_tfServername.setColumns(10);
		_pnlLogIn.add(_pnlUsername);
		_pnlUsername.add(_lblUsername);_pnlUsername.add(_tfUsername);
		_pnlLogIn.add(_pnlServername);
		_lblServername.setToolTipText("The Local iP address");
		_pnlServername.add(_lblServername);_pnlServername.add(_tfServername);
		_cbConnectedHost.setToolTipText("CHOOSE FROM A CONNECTED HOST TO REQUEST TO JOIN ONE OF THEIR CHATROOMS.");
		// set model to droplist
		_cbConnectedHost.setModel(friendsList);
		_btnLogIn.setToolTipText("CLICK TO LOG IN.");
		_btnLogIn.addActionListener(new ActionListener() { // log in
			public void actionPerformed(ActionEvent e) {
				_mainModel.login();
				// show RMI server port and User Stub port;
				_tfServerPort.setText("" + _mainModel.getServerPort());
				_tfBoundPort.setText("" + _mainModel.getUserStubPort());
				// set friend list.
				//_cbConnectedHost = new JComboBox<IChatUser>(mainModel.getFriendList());
			}
		});
		_pnlLogIn.add(_btnLogIn);
		
		_pnlNewChatRoom.setBorder(new TitledBorder(null, "New Chat Room", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_pnlNewChatRoom.setLayout(new GridLayout(2, 1, 0, 0));
		_pnlCtrl.add(_pnlNewChatRoom);
		_btnCreateChatRoom.addActionListener(new ActionListener() { // createChatroom
			public void actionPerformed(ActionEvent e) {
				_mainModel.createChatroom(_tfChatroomName.getText());
			}
		});
		_btnCreateChatRoom.setToolTipText("Create a new chat room with your customized room name.");
		_tfChatroomName.setToolTipText("Name a new chat room.");
		_tfChatroomName.setText("chatroom name");
		_tfChatroomName.setColumns(10);
		_pnlNewChatRoom.add(_tfChatroomName);
		_pnlNewChatRoom.add(_btnCreateChatRoom);
		
		_pnlConnectTo.setBorder(new TitledBorder(null, "Connect To", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_pnlCtrl.add(_pnlConnectTo);
		_pnlConnectTo.setLayout(new GridLayout(2, 1, 0, 0));
		_tfRemoteIP.setToolTipText("Please enter the IP of remote machine that you wish to connect with");
		_tfRemoteIP.setText("remote IP");
		_tfRemoteIP.setColumns(10);
		_pnlConnectTo.add(_tfRemoteIP);
		_btnConnectTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_mainModel.connectTo(_tfRemoteIP.getText());
			}
		});
		_btnConnectTo.setToolTipText("Click to connect to the remote user.");
		_pnlConnectTo.add(_btnConnectTo);
		_pnlConnectedHost.setBorder(new TitledBorder(null, "ConnectedHost", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		_pnlCtrl.add(_pnlConnectedHost);
		_pnlConnectedHost.setLayout(new GridLayout(3, 1, 0, 0));
		_pnlConnectedHost.add(_cbConnectedHost);
		
		_btnRequest.addActionListener(new ActionListener() { // request
			public void actionPerformed(ActionEvent e) {
				//1. pop out a window containing a chatroom list
				Object[] chatroomsToChoose = null;
				try {
					System.out.println("<<<ChatAppGUI.requestButton>>> IUserStub ::  " + _cbConnectedHost.getItemAt(_cbConnectedHost.getSelectedIndex()));
					System.out.println("<<<ChatAppGUI.requestButton>>> chatrooms::  " + _cbConnectedHost.getItemAt(_cbConnectedHost.getSelectedIndex()).getChatRooms());
					chatroomsToChoose = _cbConnectedHost.getItemAt(_cbConnectedHost.getSelectedIndex()).getChatRooms().toArray();
				} catch (RemoteException e1) {
					System.err.println("[ChatAppGUI.requestButton: ] Exception getting chatrooms.");
					e1.printStackTrace();
				} catch (Exception e2) {
					System.out.println("[ChatAppGUI.requestButton: ] Non-remote exception getting chatrooms.");
					e2.printStackTrace();
				}
				//2. get the selected chatroom
				if (chatroomsToChoose.length == 0) {
					return;
				}
				IChatRoom chatroomToBeJoined = (IChatRoom) JOptionPane.showInputDialog(
						_pnlCtrl, "Please choose a chatroom to join.", "Available Chatrooms", JOptionPane.PLAIN_MESSAGE, null, chatroomsToChoose, chatroomsToChoose[0]);
				//3. join a chat room 
				//3.1 create a local chatroom (recorded locally), 
				//3.2 copy all the receivers to the chatroom
				//3.3 create local receiver and put the receiverStub into the chatroom.
				//3.3 let the chatroom send a AddMeMsg to everyone else in the chatroom.
				//3.2 create a miniView for the chatroom
				_mainModel.joinChatroom(chatroomToBeJoined);
			}
		});
		_btnRequest.setToolTipText("Request to join a chatroom that a friend is in.");
		_pnlConnectedHost.add(_btnRequest);
		_btnLogOut.addActionListener(new ActionListener() { //log out
			public void actionPerformed(ActionEvent e) {
				_mainModel.logout();
			}
		});
		_btnLogOut.setToolTipText("Click to quit the ChatApp.");
		
		_pnlCtrl.add(_btnLogOut);
		
		
		
		/**
		_btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);  // for single file selection
				int returnVal = fileChooser.showOpenDialog(null);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					System.out.println("You chose to open this file: " + f.getName()
							+ " " + f.getAbsolutePath());
					// further processing of the "f" File object.
				}
			}
		});
		contentPane.add(_btnTest, BorderLayout.CENTER);
		*/
		
		contentPane.add(_tpDisplay, BorderLayout.CENTER);
		
		_tpDisplay.addTab("Info", null, _pnlInfo, null);
		_pnlInfo.setLayout(new BorderLayout(0, 0));
		_pnlInfo.add(_spInfo, BorderLayout.CENTER);
		_spInfo.setViewportView(_taInfo);
		_taInfo.setLineWrap(true);
		
		
		//_tpDisplay.addTab("Chat1", null, new MiniView(null), null);
	}
	

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatAppGUI frame = new ChatAppGUI(null);
					frame.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Start the GUI.
	 */
	public void start() {
		this.setVisible(true);
	}
	
	/**
	 * Getter for user name
	 * @return user name
	 */
	public String getUserName() {
		return _tfUsername.getText();
	}
	
	/**
	 * Display the local IP address on the GUI
	 * @param localIP local ip address.
	 */
	public void displayLocalIP(String localIP) {
		_tfServername.setText(localIP);
	}
	
	/**
	 * Install a mini View for a chatroom -- a jpanel will be added to the tabbed panel.
	 * @param chatroomMiniView The JPanel to add.
	 * @param name The name of the JPanel.
	 */
	public void installChatRoom(MiniView chatroomMiniView, String name) {
		_tpDisplay.addTab(name, null, chatroomMiniView, null);
	}
	
	/**
	 * Install a nonScrollableComponent, e.g. a game, to the main view.
	 * @param comp The component to install
	 * @param label The label of the component.
	 */
	public void installNonScrollableComponent(Component comp, String label) {
		_tpDisplay.addTab(label, null, comp, null);
	}
	
	/**
	 * Remove Chatroom's GUI.
	 * @param chatroomMiniView The miniview of the chatroom to remove.
	 */
	public void removeChatRoom(MiniView chatroomMiniView) {
		_tpDisplay.remove(chatroomMiniView);
	}
	
	/**
	 * Append infomation to info panel.
	 * @param s The information to append.
	 */
	public void appendInfo(String s) {
		_taInfo.append(s+ "\n");
	}
	
	/**
	 * Getter for User list.
	 * @return The default combo box model for the user list.
	 */
	public DefaultComboBoxModel<IUser> getCBModel() {
		return friendsList;
	}

}
