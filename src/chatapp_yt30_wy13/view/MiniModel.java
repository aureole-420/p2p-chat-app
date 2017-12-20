package chatapp_yt30_wy13.view;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import chatapp_yt30_wy13.impl.ChatRoom;
import chatapp_yt30_wy13.impl.ProxyUser;
import chatapp_yt30_wy13.impl.RemoveReceiverType;
import chatapp_yt30_wy13.impl.msg.AddReceiverType;
import chatapp_yt30_wy13.impl.msg.ImageMsg;
import chatapp_yt30_wy13.impl.msg.TextMsg;
import common.DataPacketChatRoom;
import common.IAddReceiverType;
import common.IChatRoom;
import common.IReceiver;
import common.IRemoveReceiverType;
import common.IUser;

/**
 * The mini model for a chatroom.
 * @author yt30, wy13.
 */
public class MiniModel {
	/**
	 * The mini model to view adapter.
	 */
	private IMiniModel2ViewAdapter _view;
	/**
	 * The associated chatroom.
	 */
	private IChatRoom theChatRoom;
	//private ILocalViewAdapter localViewAdapter;
	/**
	 * Associated local receiver stub.
	 */
	private IReceiver _localReceiverStub;
	
	/**
	 * Constructor of mini model
	 * @param cr the associated chatroom.
	 * @param newReceiverStub the associated receiver stub.
	 * @param _view the minimodel to view adapter
	 */
	public MiniModel(IChatRoom cr, IReceiver newReceiverStub, IMiniModel2ViewAdapter _view) {
		this._view = _view;
		this.theChatRoom = cr;
		this._localReceiverStub = newReceiverStub;
		/**
		localViewAdapter = new ILocalViewAdapter () {
			@Override
			public void display(String msg) {
				_view.display(msg);
			}

			@Override
			public void displayImage(ImageIcon img) {
				_view.displayImage(img);
				
			}
		};
		((ChatRoom) theChatRoom).setLocalViewAdapter(localViewAdapter);
		*/
	}
	
	/**
	 * display a msg to mini view.
	 * @param msg The msg to display.
	 */
	public void display(String msg) {
		_view.display(msg);
	}
	/**
	 * getter for the receiver stubs of this chatroom.
	 * @return
	 */
	public Collection<IReceiver> getReceiver() {
		return theChatRoom.getIReceiverStubs();
	}
	/**
	 * send a text datapacket.
	 * @param text The text to be sent.
	 */
	public void sendText(String text) {
		theChatRoom.sendPacket(new DataPacketChatRoom<TextMsg>(TextMsg.class, new TextMsg(text), ((ChatRoom) theChatRoom).getLocalIReceiverStub()));
	}
	
	/**
	 * Send an image datapacket.
	 * @param image The image to be sent.
	 */
	public void sendImage(ImageIcon image) {
		theChatRoom.sendPacket(new DataPacketChatRoom<ImageMsg>(ImageMsg.class, new ImageMsg(image), ((ChatRoom) theChatRoom).getLocalIReceiverStub()));
	}
	/**
	 * refresh the DefaultListModel for the chatter list
	 * @return the DefaultListModel for the chatter list
	 */
	public DefaultListModel<IUser> refreshChatterList() {
		DefaultListModel<IUser> list = new DefaultListModel<>();
		for (IReceiver r: theChatRoom.getIReceiverStubs()) {
			try {
				list.addElement(new ProxyUser(r.getUserStub()));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	/**
	 * The miniModel is created when when I joining an existing chatroom -- theChatRoom.
	 * This method is called to notify all existing chatters that they need to add my IReceiverStub to their chatroom.  
	 */
	public void notifyJoin() {
		theChatRoom.sendPacket(new DataPacketChatRoom<IAddReceiverType>(IAddReceiverType.class, 
				new AddReceiverType(_localReceiverStub), _localReceiverStub));
		theChatRoom.addIReceiverStubLocally(_localReceiverStub);
	}
	
	/**
	 * This method is called to notify all existing chatters that they need to remove my IReceiverStub from their chatroom.
	 */
	public void notifyLeave() {
		theChatRoom.removeIReceiverStubLocally(_localReceiverStub);
		theChatRoom.sendPacket(new DataPacketChatRoom<IRemoveReceiverType>(IRemoveReceiverType.class, 
				new RemoveReceiverType(_localReceiverStub), _localReceiverStub));
		((ChatRoom) theChatRoom).clearChatRoom();
		_view.quitChatRoom(theChatRoom);
		// may need to close mini view?
	}

}
