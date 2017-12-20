package chatapp_yt30_wy13.view;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import common.IReceiver;
import common.IUser;

/**
 * Adapter that allows mini view to call model's methods.
 * @author yt30, wy13.
 *
 */
public interface IMiniView2ModelAdapter {
	
	/**
	 * send text data packet
	 * @param text Text to send 
	 */
	void sendText(String text);
	
	/**
	 * send image data packet 
	 * @param imageIcon image icon to send
	 */
	void sendImage(ImageIcon imageIcon);
	
	/**
	 * refresh chatter list of mini view.
	 * @return The defaultListModel of chatter list.
	 */
	DefaultListModel<IUser> getRefreshedChatterList();
	
	/**
	 * notifying other chatters that I am leaving the chatroom.
	 */
	void notifyLeave();

}
