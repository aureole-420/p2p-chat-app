package chatapp_yt30_wy13.model;

import java.awt.Component;

import javax.swing.DefaultComboBoxModel;

import chatapp_yt30_wy13.view.MiniView;
import common.IChatRoom;
import common.IReceiver;
import common.IUser;

/**
 * Adapter that allows main model to call main view's method.
 * @author yt30, wy13.
 */
public interface IModel2ViewAdapter {
	
	/**
	 * get username of local user.
	 * @return
	 */
	public String getUserName(); // from mainView's tf to get username
	
	/**
	 * dispaly Local ip address
	 * @param localIP local ip address.
	 */
	public void displayLocalIP(String localIP);
	
	/**
	 * append information on default view panel (not chat room's panel.)
	 * @param s The information to append
	 */
	public void info(String s);
	
	/**
	 * get Combo box model of main view.
	 * @return the default combo box model, by update which the main view User list will also be updated.
	 */
	public DefaultComboBoxModel<IUser> getCBModel();

	/**
	 * Make a mini MVC for the chat room.
	 * @param chatroom The newly created local chat room 
	 * @param newReceiverStub The receiver stub of the newly created chat room.
	 * @return The adapter that allows model to access miniMVC's method.
	 */
	IMiniMVCAdapter makeMiniMVC(IChatRoom chatroom, IReceiver newReceiverStub);

	/**
	 * remove a chatroom's mini view from main view. 
	 * @param miniView The mini View of the chat room to be removed.
	 */
	public void removeChatRoom(MiniView miniView);
	
	/**
	 * Install a nonScrollableComponent, e.g. a game, to the main view.
	 * @param comp The component to install
	 * @param label The label of the component.
	 */
	public void installNonScrollabelComponent(Component comp, String label);
}
