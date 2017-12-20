package chatapp_yt30_wy13.impl;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.UUID;

import chatapp_yt30_wy13.view.ILocalViewAdapter;
import common.IReceiver;
import common.DataPacketChatRoom;
import common.IChatRoom;


/**
 * Implementation of IChatRoom.
 * @author yt30, wy13
 */
public class ChatRoom implements IChatRoom {
	/**
	 * generaated serial id.
	 */
	private static final long serialVersionUID = 44227815336998084L;
	/**
	 * uuid of the chat room
	 */
	private UUID uuid = UUID.randomUUID();
	/**
	 * The name of the chatroom
	 */
	private String chatroomName = "defaultChatroom";
	/**
	 * The receiver stubs of all chatters in this chatroom.
	 */
	private HashSet<IReceiver> receiverStubs = new HashSet<IReceiver>();
	/**
	 * local view adapter to allow calling miniview's method.
	 */
	private transient ILocalViewAdapter miniViewAdapter;
	/**
	 * local receiver stub ( corresponding to the chatroom and local user.)
	 */
	private IReceiver localReceiverStub = null;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return chatroomName;
	}
	
	/**
	 * Setter for the chatroom name
	 * @param name the name of chat room.
	 */
	public void setName(String name) {
		this.chatroomName = name;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashSet<IReceiver> getIReceiverStubs() {
		return receiverStubs;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addIReceiverStubLocally(IReceiver receiver) {
		receiverStubs.add(receiver);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeIReceiverStubLocally(IReceiver receiver) {
		receiverStubs.remove(receiver);
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <D> void sendPacket(DataPacketChatRoom<D> Msg) {
		System.out.println("\n <<<Chatroom.dispatchMsg()>>> " + receiverStubs.size() + " receivers to be dispatched.\n " );
		for (IReceiver receiver : receiverStubs) {
			try {
				System.out.println("<<<Chatroom.sendPacket()>>> receiver:: " + chatroomName + " :: " + receiver.toString());
				receiver.receive(Msg);
				System.out.println("[Chatroom.sendPacket()]: successfully dispatch msg to " + receiver);
				//miniViewAdapter.display("join the room");
			} catch (RemoteException e) {
				System.err.println("[Chatroom.sendPacket"
						+ "()]: exception dispatching msg to receiver::" + receiver);
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	/**
	 * Setter for local receiver stub.
	 * @param lrStub The local receive stub.
	 */
	public void setLocalIReceiverStub(IReceiver lrStub) {
		this.localReceiverStub = lrStub;
	}
	
	/**
	 * getter of local receiver stub.
	 * @return local receiver stub.
	 */
	public IReceiver getLocalIReceiverStub() {
		return localReceiverStub;
	}

	/**
	 * setter for local view adapter 
	 * @param viewAdapter adapter allows chat room to call view's method.
	 */
	public void setLocalViewAdapter(ILocalViewAdapter viewAdapter) {
		this.miniViewAdapter = viewAdapter;
	}
	
	/**
	 * {@inheritDoc}<br/>
	 */
	@Override
	public String toString() {
		return chatroomName;
	}

	/**
	 * Clear all receivers in the chatroom's receiver stub list.
	 * Called when one is quiting this chatroom.
	 */
	public void clearChatRoom() {
		receiverStubs.clear();
	}




}
