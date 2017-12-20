package chatapp_yt30_wy13.model;

import java.awt.Component;

import chatapp_yt30_wy13.view.MiniModel;
import chatapp_yt30_wy13.view.MiniView;

/**
 * Adapter that allows main model to call miniMVC's method.
 * @author yt30, wy13
 */
public interface IMiniMVCAdapter {
	
	/**
	 * getter for mini Model.
	 * @return mini model of a chat room.
	 */
	public MiniModel getMiniModel();
	
	/**
	 * getter for mini view.
	 * @return mini view of a chat room.
	 */
	public MiniView getMiniView();

	/**
	 * This method is called whenever a chatter joins/leaves the chat room.
	 * So that the chatter list of mini view will be updated automatically.
	 */
	public void refreshChatterList();
	
	/**
	 * append test msg to the mini view.
	 * @param text The text to append.
	 */
	public void appendMsg(String text);
	
	/**
	 * component to be added to the mini view.
	 * @param compToBeAdded
	 */
	public void addScrollableComponentToTab(Component compToBeAdded);

	/**
	 * notify every chatter in the chat room that I (local user) is joining this chat room.
	 */
	public void notifyJoin();

}
