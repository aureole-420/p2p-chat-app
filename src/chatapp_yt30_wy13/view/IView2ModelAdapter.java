package chatapp_yt30_wy13.view;

import common.IChatRoom;

/**
 * Adapter that allows main view to call main Model's methods.
 * @author yuhui
 *
 */
public interface IView2ModelAdapter {
	
	/**
	 * register the user.
	 */
	public void login();
	/**
	 * create a chatroom.
	 * @param chatroomName The name of chatroom.
	 */
	public void createChatroom(String chatroomName);
	/**
	 * set to normal mode
	 */
	public void setNormalMode();
	/**
	 * set to test mode.
	 */
	public void setTestMode();
	
	/**
	 * getter for user stub port
	 * @return the user stub port.
	 */
	public int getUserStubPort();
	/**
	 * getter for the server stub port.
	 * @return The server stub port.
	 */
	public int getServerPort();
	/**
	 * connect To a remote host.
	 * @param remoteHostIP The IP address of a remote host.
	 */
	public void connectTo(String remoteHostIP);
	/**
	 * log out
	 */
	public void logout();
	/**
	 * join a chat room
	 * @param chatroomToBeJoined The chat room to join.
	 */
	public void joinChatroom(IChatRoom chatroomToBeJoined);
}
