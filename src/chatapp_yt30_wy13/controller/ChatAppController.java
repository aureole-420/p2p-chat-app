package chatapp_yt30_wy13.controller;

import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.DefaultComboBoxModel;

import chatapp_yt30_wy13.model.ChatAppModel;
import chatapp_yt30_wy13.model.IMiniMVCAdapter;
import chatapp_yt30_wy13.model.IModel2ViewAdapter;
import chatapp_yt30_wy13.model.MiniController;
import chatapp_yt30_wy13.view.ChatAppGUI;
import chatapp_yt30_wy13.view.IView2ModelAdapter;
import chatapp_yt30_wy13.view.MiniModel;
import chatapp_yt30_wy13.view.MiniView;
import common.IReceiver;
import common.IChatRoom;
import common.IUser;


/**
 * Controller of the rmi chat app.
 * @author yt30, yw13
 */
public class ChatAppController {
	
	/**
	 * main model
	 */
	private ChatAppModel _model;
	
	/**
	 * main view.
	 */
	private ChatAppGUI _view;
	
	/**
	 * constructor of the controller.
	 */
	public ChatAppController() {
		_model = new ChatAppModel(new IModel2ViewAdapter() {

			@Override
			public String getUserName() {
				return _view.getUserName();
			}

			@Override
			public void displayLocalIP(String localIP) {
				_view.displayLocalIP(localIP);
			}

			@Override
			public IMiniMVCAdapter makeMiniMVC(IChatRoom chatroom, IReceiver newReceiverStub) {
				MiniController miniController = new MiniController(chatroom, newReceiverStub, _model);
			    //install mini-View to main-View
				_view.installChatRoom(miniController.getMiniView(), chatroom.getName());
				
				 return new IMiniMVCAdapter() {

					@Override
					public MiniModel getMiniModel() {
						return miniController.getMiniModel();
					}

					@Override
					public MiniView getMiniView() {
						return miniController.getMiniView();
					}


					@Override
					public void refreshChatterList() {
						miniController.getMiniView().refreshChatterList();
					}

					@Override
					public void appendMsg(String text) {
						miniController.getMiniView().append(text);
					}

					@Override
					public void addScrollableComponentToTab(Component compToBeAdded) {
						miniController.getMiniView().addToTab(compToBeAdded);
					}

					@Override
					public void notifyJoin() {
						miniController.getMiniModel().notifyJoin();
					}
				 };
			}

			@Override
			public void info(String s) {
				_view.appendInfo(s);
			}

			@Override
			public DefaultComboBoxModel<IUser> getCBModel() {
				return _view.getCBModel();
			}

			@Override
			public void removeChatRoom(MiniView chatroomMiniView) {
				_view.removeChatRoom(chatroomMiniView);
			}

			@Override
			public void installNonScrollabelComponent(Component comp, String label) {
				_view.installNonScrollableComponent(comp, label);
				
			}


			
		});
		
		_view = new ChatAppGUI(new IView2ModelAdapter() {

			@Override
			public void login() {
				_model.login();
			}

			@Override
			public void createChatroom(String chatroomName) {
				_model.createChatroom(chatroomName);
			}

			@Override
			public void setNormalMode() {
				_model.setNormalMode();
			}

			@Override
			public void setTestMode() {
				_model.setTestMode();
			}

			@Override
			public int getUserStubPort() {
				return _model.getUserStubPort();
			}

			@Override
			public int getServerPort() {
				return _model.getServerPort();
			}

			@Override
			public void connectTo(String remoteHostIP) {
				_model.connectTo(remoteHostIP);
			}

			@Override
			public void logout() {
				_model.stop();
			}

			@Override
			public void joinChatroom(IChatRoom chatroomToBeJoined) {
				_model.joinChatroom(chatroomToBeJoined);
			}
			
		});
	}
	
	/**
	 * start the controller.
	 */
	public void start() {
		_model.start();
		_view.start();
	}
	
	/**
	 * main method of the app.
	 * @param args default string array.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			// java specs say that the system must be constructed on the GUI event thread
			@Override
			public void run() {
				try {
					ChatAppController controller = new ChatAppController();
					controller.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
