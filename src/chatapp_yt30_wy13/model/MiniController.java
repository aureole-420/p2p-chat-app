package chatapp_yt30_wy13.model;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import chatapp_yt30_wy13.view.IMiniModel2ViewAdapter;
import chatapp_yt30_wy13.view.IMiniView2ModelAdapter;
import chatapp_yt30_wy13.view.MiniModel;
import chatapp_yt30_wy13.view.MiniView;
import common.IChatRoom;
import common.IReceiver;
import common.IUser;

/**
 * Controller of a mini MVC for a chatroom.
 * @author yt30, wy13
 */
public class MiniController {
	/**
	 * mini view for the chat room.
	 */
	private MiniView _miniView = null;
	
	/**
	 * mini mode for the chat room.
	 */
	private MiniModel _miniModel = null;
	
	/**
	 * constructor of the mini controller.
	 * @param chatroom The chat room corresponding to the mini MVC
	 * @param newReceiverStub The receiver stub corresponding to the mini mvc.
	 * @param mainModel main model.
	 */
	public MiniController(IChatRoom chatroom, IReceiver newReceiverStub, ChatAppModel mainModel) {
			
		
		 _miniModel = new MiniModel(chatroom, newReceiverStub, new IMiniModel2ViewAdapter() {
			@Override
			public void display(String msg) {
				_miniView.append(msg);
			}

			@Override
			public void displayImage(ImageIcon img) {
				_miniView.appendImage(img);
				
			}

			@Override
			public void quitChatRoom(IChatRoom theChatRoom) {
				mainModel.quitChatRoom(theChatRoom);
			}	 
		});
			 
		
		 _miniView = new MiniView(new IMiniView2ModelAdapter() {

			@Override
			public void sendText(String text) {
				_miniModel.sendText(text);
			}

			@Override
			public void sendImage(ImageIcon image) {
				_miniModel.sendImage(image);
			}

			@Override
			public DefaultListModel<IUser> getRefreshedChatterList() {
				return _miniModel.refreshChatterList();
			}

			@Override
			public void notifyLeave() {
				_miniModel.notifyLeave();
			}
				 
		});
	}
	
	/**
	 * getter for mini view
	 * @return mini view of the mini mvc.
	 */
	public MiniView getMiniView() {
		return _miniView;
	}
	
	/**
	 *  getter for mini model
	 * @return mini model of the mini mvc.
	 */
	public MiniModel getMiniModel() {
		return _miniModel;
	}
}
