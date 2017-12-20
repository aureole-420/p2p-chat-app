package chatapp_yt30_wy13.view;

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import java.awt.BorderLayout;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import common.IUser;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

/**
 * Mini view for the chatroom.
 * @author yt30, wy13.
 *
 */
public class MiniView extends JPanel {
	
	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = -5381913470397847901L;
	/**
	 * control panel for the mini view.
	 */
	private final JPanel _pnlCtrl = new JPanel();
	/**
	 * text field for chatting.
	 */
	private final JTextField _tfChatInput = new JTextField();
	/**
	 * Click the button to send a text msg.
	 */
	private final JButton _btnTxt = new JButton("send text");
	/**
	 * the panel for chatter list.
	 */
	private final JPanel _pnlChatterList = new JPanel();
	/**
	 * a jlist of chatters.
	 */
	private final JList<IUser> _listChatter = new JList<>();
	/**
	 * click the button to send an image.
	 */
	private final JButton _btnSendImg = new JButton("send img");
	/**
	 * the label for chatters.
	 */
	private final JLabel _lblChatters = new JLabel("Chatters");
	/**
	 * The scroll panel for displaying msgs.
	 */
	private final JScrollPane _spDisplay = new JScrollPane();
	/**
	 * The text panel to display msgs.
	 */
	private final JTextPane _tpDisplay = new JTextPane();
	/**
	 * The defaultlist model for the list of chatters.
	 */
	private DefaultListModel<IUser> _chatterListModel = new DefaultListModel<>();

	/**
	 * The adapter allowing miniview to use model's adapter.
	 */
	private IMiniView2ModelAdapter _miniModel;
	/**
	 * click the button to leave the chat room.
	 */
	private final JButton _btnLeaveChatRoom = new JButton("Leave");
	
	/**
	 * constructor of mini view.
	 * @param m An instance of IMiniView2ModelAdapter.
	 */
	public MiniView(IMiniView2ModelAdapter m) {
		this._miniModel = m; 
		initGUI();
	}
	
	/**
	 * Initialize the GUI.
	 */
	private void initGUI() {
		
		setLayout(new BorderLayout(0, 0));
		
		setPreferredSize(new Dimension(600,300));
		
		_pnlCtrl.setBackground(Color.WHITE);
		
		add(_pnlCtrl, BorderLayout.SOUTH);
		_btnLeaveChatRoom.addActionListener(new ActionListener() { // leave the chatroom
			public void actionPerformed(ActionEvent e) {
				_miniModel.notifyLeave();
			}
		});
		_btnLeaveChatRoom.setToolTipText("Click to leave this chat room.");
		
		_pnlCtrl.add(_btnLeaveChatRoom);
		_tfChatInput.setToolTipText("input string in the text field and click the send text button to send the msg.");
		_pnlCtrl.add(_tfChatInput);
		_btnTxt.setToolTipText("click to send the text msg");
		_btnTxt.addActionListener(new ActionListener() { // send text
			public void actionPerformed(ActionEvent e) {
				if (_tfChatInput.getText() != null) {
					_miniModel.sendText(_tfChatInput.getText());
				}
				
			}
		});
		_pnlCtrl.add(_btnTxt);
		_btnSendImg.setToolTipText("click to send an image.");
		
		_pnlCtrl.add(_btnSendImg);
		_btnSendImg.addActionListener(new ActionListener() { // send image
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);  // for single file selection
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = fileChooser.getSelectedFile();
					System.out.println("You chose to open this file: " + f.getName()
							+ " " + f.getAbsolutePath());
					// further processing of the "f" File object.
					//System.out.println(f.getPath());
					Image image = null;
					try {
						image = ImageIO.read(f);
						_miniModel.sendImage(new ImageIcon(image));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// appendImage(new ImageIcon(image));
				}
			}
		});
		
		_tfChatInput.setText("Input string and click the  SEND TEXT button");
		_tfChatInput.setColumns(20);
		_pnlChatterList.setToolTipText("DISPLAYING A LIST OF CHATTERS.");
		_pnlChatterList.setBackground(Color.WHITE);
		
		_pnlChatterList.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		add(_pnlChatterList, BorderLayout.WEST);
		_pnlChatterList.setLayout(new BoxLayout(_pnlChatterList, BoxLayout.Y_AXIS));
		_lblChatters.setHorizontalAlignment(SwingConstants.CENTER);
		_lblChatters.setBackground(Color.LIGHT_GRAY);
		
		_pnlChatterList.add(_lblChatters);
		_pnlChatterList.add(_listChatter);
		//_listChatter.setListData(new String[] {"a", "b"}); // should get user from ...
		_listChatter.setModel(_chatterListModel);
		
		add(_spDisplay, BorderLayout.CENTER);
		//_spDisplay.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		_tpDisplay.setBackground(Color.WHITE);
		_tpDisplay.setToolTipText("Chatting history shown here.");
		_spDisplay.setViewportView(_tpDisplay);
		//append("SSDFEFe");
		//_tpDisplay.insertIcon( new ImageIcon("/Users/yuhui/Desktop/sample.png"));
		
		//_tpDisplay.insertComponent(new JLabel(new ImageIcon("/Users/yuhui/Desktop/sample.png")));
		/**
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(false);  // for single file selection
		int returnVal = fileChooser.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			System.out.println("You chose to open this file: " + f.getName()
					+ " " + f.getAbsolutePath());
			// further processing of the "f" File object.
			System.out.println(f.getPath());
		}
		*/
	}
	
	/**
	 * Append a string msg to the GUI.
	 * @param s The string msg to append 
	 */
	public void append(String s) {
		try {
			Document doc = _tpDisplay.getDocument();
			doc.insertString(doc.getLength(), "\n"+ s + "\n", null);
			_tpDisplay.setCaretPosition(_tpDisplay.getDocument().getLength());
		} catch (BadLocationException e) {
			System.out.println("<<<MiniView.append()>>> Exception appending TEXT msg to Chatroom: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * Append an image to the GUI.
	 * @param img The image to append.
	 */
	public void appendImage(ImageIcon img) {
		try {
			//append("\n");
			//JLabel label = new JLabel(img);
			//_tpDisplay.insertComponent(label);
			_tpDisplay.insertIcon(img);
			//append("\n");
			_tpDisplay.setCaretPosition(_tpDisplay.getDocument().getLength());
		} catch (Exception e) {
			System.out.println("<<<MiniView.appendImage()>>> Exception appending TEXT msg to Chatroom: ");
			e.printStackTrace();
		}
	}
	
	/**
	 * refresh chatter list.
	 */
	public void refreshChatterList() {
		//_chatterListModel.clear();
		DefaultListModel<IUser> temp = _miniModel.getRefreshedChatterList();
		 _chatterListModel.clear();
		 for (Object r :  temp.toArray()) {
			 _chatterListModel.addElement((IUser) r);
		 }
		//System.out.println("\n%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%"+_chatterListModel.getElementAt(0));
	}
	/**
	 * add an scrollable component to the tabbed panel.
	 * @param compToBeAdded
	 */
	public void addToTab(Component compToBeAdded) {
		_tpDisplay.insertComponent(compToBeAdded);
		//append("\n ###### Trying to add an image to tab \n");
	}

	/**
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame f = new JFrame("Panel example");
					f.setSize(500, 300);
					f.getContentPane().add(new MiniView(null));
					f.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	*/
	
	
	
}
