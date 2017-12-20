package chatapp_yt30_wy13.view;

import javax.swing.ImageIcon;

import common.IChatRoom;

/**
 * Adapter that allows mini model to call View (mini view, main view, main model)'s method.
 * @author yt30, wy13.
 */
public interface IMiniModel2ViewAdapter {
	/**
	 * Display a string message on mini view.
	 * @param msg msg to display.
	 */
	public void display(String msg);
	
	/**
	 * Display an image on the mini view 
	 * @param img Image to display.
	 */
	public void displayImage(ImageIcon img);

	/**
	 * Quit the chatroom.
	 * @param theChatRoom The chatroom to quit.
	 */
	public void quitChatRoom(IChatRoom theChatRoom);
}
