package chatapp_yt30_wy13.impl;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import javax.swing.DefaultComboBoxModel;

import common.IChatRoom;
import common.IUser;


/**
 * Implementation of User.
 * @author yt30, wy13.
 *
 */
public class User implements IUser {
	/**
	 * uuid of user object.
	 */
	private UUID uuid = UUID.randomUUID();
	/**
	 * Name of the user.
	 */
	private String userName;
	/**
	 * All connected user
	 */
	private HashMap<UUID, IUser> friends = new HashMap<UUID, IUser>();
	/**
	 * a list of connected user, used for main view.
	 */
	private DefaultComboBoxModel<IUser> friendsList = null;
	/**
	 * A collection of chatroom that a user has joined.
	 */
	private Collection<IChatRoom> chatrooms = new HashSet<IChatRoom>();
	/**
	 * {@inheritDoc}
	 */
	@Override                 
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * Set username for the user. A default name can be made in the concrete subclass.
	 * @param userName The name of the user.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/**
	 * getter for a list of connected users.
	 * @return a list of connected users.
	 */
	public HashMap<UUID, IUser> getFriends() {
		return friends;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(IUser userStub) throws RemoteException {
		friends.put(userStub.getUUID(), userStub);
		friendsList.addElement(new ProxyUser(userStub));
		// possibly also have a user to view adapter? asking the view to update the friend list
	}
	
	/**
	 * Setter for friendlist
	 * @param friendsList A list of connected users.
	 */
	public void setFriendsList(DefaultComboBoxModel<IUser> friendsList) {
		this.friendsList = friendsList;
	}
	
	/**
	 * Add a chatroom to the user's chatroom list.
	 * @param room The room to add.
	 */
	public void addChatRoom(IChatRoom room) {
		chatrooms.add(room);
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() throws RemoteException {
		return userName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IChatRoom> getChatRooms() throws RemoteException {
		return chatrooms;
	}
	
	/**
	 * remove a chatroom from the user's chatroom list.
	 * @param theChatRoom The chatroom to remove.
	 * @return Whether the removal action is successfully carried out.
	 */
	public boolean removeChatRoom(IChatRoom theChatRoom) {
		return chatrooms.remove(theChatRoom);
	}
	

	
}
