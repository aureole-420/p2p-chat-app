package chatapp_yt30_wy13.model;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import chatapp_yt30_wy13.impl.ChatRoom;
import chatapp_yt30_wy13.impl.ICmd2ModelAdapterLocal;
import chatapp_yt30_wy13.impl.ProxyUser;
import chatapp_yt30_wy13.impl.Receiver;
import chatapp_yt30_wy13.impl.User;
import common.IReceiver;
import common.IChatRoom;
import common.ICmd2ModelAdapter;
import common.IComponentFactory;
import common.IUser;
import provided.rmiUtils.IRMI_Defs;
import provided.rmiUtils.RMIUtils;

/**
 * Main model for RMI chat app
 * @author yt30, wy13
 */
public class ChatAppModel {
	
	/**
	 * Comsumer object used to initiate RMIutils object.
	 */
	private Consumer<String> outputCmd = new Consumer<String>() {
		@Override
		public void accept(String t) {
			System.out.println("[ChapAppModel.outputCmd]" + t);
		}
	};
	
	/**
	 * Number of chat rooms. 
	 */
	private int countChatroom = 0;
	
	/**
	 * The RMIutils object used to start rmi server
	 */
	private RMIUtils _rmiUtils = new RMIUtils(outputCmd);
	
	/**
	 * local registry
	 */
	private Registry _registry;
	
	/**
	 * local User object (can be accessed remotely through IUser API)
	 */
	private User _localUser;
	
	/**
	 * local user stub for the User object.
	 */
	private IUser _localUserStub;
	
	/**
	 * The view adapter allows main model to talk to main view 
	 */
	private IModel2ViewAdapter mainView;
	
	/**
	 * A set of friends (connected users).
	 */
	private HashMap<UUID, IUser> _friends;
	
	/**
	 * Local IP address.
	 */
	private String _localIP;
	
	/**
	 * a map to store local miniMVC adapters.
	 */
	private HashMap<UUID, IMiniMVCAdapter> miniMVCs = new HashMap<UUID, IMiniMVCAdapter>();
	
	/**
	 * a map to store local remote object (every chatroom corresponds to a local remote object.) 
	 */
	private HashMap<UUID, IReceiver> _localReceiverObj = new HashMap<UUID, IReceiver>(); // store the remote object;
	
	/**
	 * default port for RMI server 
	 */
	int RMI_SERVER_PORT = 2001; //IRMI_Defs.CLASS_SERVER_PORT_SERVER; //2001
	/**
	 * default port for User stub.
	 */
	int MAIN_STUB_PORT = 2100; // IUser.BOUND_PORT; //2100
	/**
	 * default port for IReceiver stub.
	 */
	int RECEIVER_STUB_PORT = 2102; //IReceiver.BOUND_PORT_CLIENT;
	/**
	 * default local IUser stub name (with which remote client may lookup on our registry )
	 */
	String LOCAL_STUB_NAME = "USER";
	
	/**
	 * default remote IUser Stub name (with which we may lookup on remote registry).
	 */
	String REMOTE_STUB_NAME = "USER";
	
	/**
	 * Constructor for main model
	 * @param mainView View adapter that allows model to call View's method.
	 */
	public ChatAppModel(IModel2ViewAdapter mainView) {
		this.mainView = mainView;
	}
	
	/**
	 * start the model.
	 */
	public void start() {
		// normal mode;
	}
	
	/**
	 * ONLY used when testing two chat app on a SINGLE computer.
	 * one client must be set to NORMAL MODE and the other TESTING MODE.
	 */
	public void setNormalMode() {
		RMI_SERVER_PORT = 2001; // IRMI_Defs.CLASS_SERVER_PORT_SERVER; //2001
		MAIN_STUB_PORT = 2100; // IUser.BOUND_PORT; //2100
		LOCAL_STUB_NAME = "USER";
		REMOTE_STUB_NAME = "TEST";
		RECEIVER_STUB_PORT = 2102; // IReceiver.BOUND_PORT_CLIENT; // 2102
		mainView.info("[ChatAppModel.setNormalMode] NORMAL_MODE chosen!");
	}
	
	/**
	 * ONLY used when testing two chat app on a SINGLE computer.
	 * one client must be set to NORMAL MODE and the other TESTING MODE.
	 */
	public void setTestMode() {
		RMI_SERVER_PORT = 2002; //IRMI_Defs.CLASS_SERVER_PORT_CLIENT;  // 2002
		MAIN_STUB_PORT = 2101; // IUser.BOUND_PORT+1; // 2101
		RECEIVER_STUB_PORT = 2152; //IReceiver.BOUND_PORT_CLIENT + 50; // 2152
		LOCAL_STUB_NAME = "TEST";
		REMOTE_STUB_NAME = "USER";
		mainView.info("[ChatAppModel.setTestMode] TEST_MODE chosen!");
	}
	
	/**
	 * Initialize the rmi server with possible mode setting.
	 */
	private void init() {
		try {
			_rmiUtils.startRMI(RMI_SERVER_PORT);
			mainView.info("[ChatAppModel.init()] start RMI server using port: " + RMI_SERVER_PORT);
			
			// this step may take longer time for the second user if there  are two users sharing the same IP.
			_registry = _rmiUtils.getLocalRegistry();
			mainView.info("[ChatAppModel.init()] get local registry: " + _registry);
			
			_localIP = _rmiUtils.getLocalAddress();
			mainView.displayLocalIP(_localIP);
		} catch(Exception e){
			System.err.println("[ChatAppModel.init()] Exception starting rmi" + e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * This method register a user on a local registry.
	 */
	public void login() {
		init();
		try {
			_localUser = new User(); // local user
			_localUser.setUserName(mainView.getUserName()+"@"+_localIP);
			_localUser.setFriendsList(mainView.getCBModel());
			_friends =  _localUser.getFriends();
			
			_localUserStub = (IUser) UnicastRemoteObject.exportObject(_localUser, MAIN_STUB_PORT);
			_registry.rebind(LOCAL_STUB_NAME, _localUserStub);
	
			
			System.out.println("[ChatAppModel.login()] successfully create IUser: " + _localUser.getUUID()  + _localUser );
			mainView.info("[ChatAppModel.login()] successfully create IUser: "+ _localUser.getName() +
					"  " + _localUser.getUUID()  + _localUser );
		} catch (RemoteException e) {
			System.err.println("[ChatAppModel.login()] Exception logging in");
			e.printStackTrace();
			System.exit(-1);
		}	
	}
	
	/**
	 * This method allows the local user to quit a chat room.
	 * @param theChatRoom The chat room to exit.
	 */
	public void quitChatRoom(IChatRoom theChatRoom) {
		IReceiver receiverOfChatRoomToQuit = ((ChatRoom) theChatRoom).getLocalIReceiverStub();
		try {
			_localReceiverObj.remove(receiverOfChatRoomToQuit.getUUID());
		} catch (RemoteException e) {
			System.out.println("<<<ChatAppModel.quitChatRoom()>>> Exception quiting chat room, failt to get UUID of the local receiver");
			e.printStackTrace();
		}
		// remove view first;
		mainView.removeChatRoom(miniMVCs.get(theChatRoom.getUUID()).getMiniView());
		miniMVCs.get(theChatRoom.getUUID()).getMiniView().append("^^^^^^^^^^^^^%%%%%%");
		miniMVCs.remove(theChatRoom.getUUID());
		_localUser.removeChatRoom(theChatRoom);
	}
	
	/**
	 * Connect to remote user -- acquire remote user stub.
	 * @param remoteHost The IP address of remote user.
	 */
	public void connectTo(String remoteHost) {
		try {
			_registry = _rmiUtils.getRemoteRegistry(remoteHost);
			IUser remoteUserStub = (IUser) _registry.lookup(REMOTE_STUB_NAME); // GET REMOTE STUB
			_friends.put(remoteUserStub.getUUID(), remoteUserStub);
			mainView.getCBModel().addElement(new ProxyUser(remoteUserStub));
			
			mainView.info("[ChatAppModel.connectTo()] successfully connected to " +  remoteUserStub.getName());
			// auto connect back
			remoteUserStub.connect(_localUserStub);
			try {remoteUserStub.getChatRooms();}
			catch(Exception e) {System.out.println("fail to get chatroom!");}
			//System.out.println("<<<connectTo>>>>" + remoteUserStub.getChatrooms());

		} catch(Exception e) {
			System.err.println("<<<ChatAppModel.connectTo()>>> Exception connecting to " + remoteHost);
		}
	}
	
	/**
	 * Create a chat room
	 * @param chatroomName The name of the new chat room.
	 */
	public void createChatroom(String chatroomName) {
		try {
			IChatRoom localChatroom = new ChatRoom();
			((ChatRoom) localChatroom).setName(chatroomName);
			
			IReceiver localReceiver = new Receiver(_localUserStub, localChatroom); // HERE localUser is not necessary?
			IReceiver localReceiverStub = (IReceiver) UnicastRemoteObject.exportObject(localReceiver, RECEIVER_STUB_PORT + countChatroom);
			((Receiver) localReceiver).setLocalReceiverStub(localReceiverStub);
			
			IMiniMVCAdapter miniMVC = mainView.makeMiniMVC(localChatroom, localReceiverStub);
			miniMVCs.put(localChatroom.getUUID(), miniMVC);
			
			/**
			 * Adapter that allows cmd to call model's methods.
			 */
			ICmd2ModelAdapter cmd2Model = new ICmd2ModelAdapterLocal() {
				
				@Override
				public void refreshChatterList() {
					miniMVC.refreshChatterList();
				}

				@Override
				public void appendMsg(String text, String name) {
					miniMVC.appendMsg(text);
				}

				@Override
				public void buildScrollableComponent(IComponentFactory fac, String label) {
					miniMVC.addScrollableComponentToTab(fac.makeComponent());	
				}

				@Override
				public void buildNonScrollableComponent(IComponentFactory fac, String label) {
					mainView.installNonScrollabelComponent(fac.makeComponent(), label);
				}
			};
			((Receiver) localReceiver).setDataPacketAlgo((ICmd2ModelAdapterLocal) cmd2Model);
		

			System.out.println("countChatroom: create chatroom at port "+ RECEIVER_STUB_PORT + countChatroom);
			countChatroom++;
			
			localChatroom.addIReceiverStubLocally(localReceiverStub);
		
			((ChatRoom) localChatroom).setLocalIReceiverStub(localReceiverStub);
			_localUser.addChatRoom(localChatroom);
				System.out.println("<<<ChatAppModel.createChatroom()>>>: successfully add chatroom " + localChatroom.getName() + "::"+localChatroom);
			
			_localReceiverObj.put(localChatroom.getUUID(), localReceiver);
			
			miniMVC.refreshChatterList();

			System.out.println(""+miniMVCs.size());
			
			System.out.println("local receiver of Chatroom:: "+ countChatroom + " ::'s receiver ---- " + localReceiverStub.toString());		

		} catch (RemoteException e) {
			System.err.println("[ChatAppModel.createChatroom()]: exception creating chatroom " + e);
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * Join a chat room.
	 * @param chatroomToBeJoined The name of chat room to join.
	 */
	public void joinChatroom(IChatRoom chatroomToBeJoined) {
		try {
			// copy a local chat room
			IChatRoom localChatroomCopy = new ChatRoom();
			((ChatRoom) localChatroomCopy).setName(chatroomToBeJoined.getName());
			for (IReceiver rStub : chatroomToBeJoined.getIReceiverStubs()) {
				localChatroomCopy.addIReceiverStubLocally(rStub);
				System.out.println("<<<test rStub>>>"+rStub.getUserStub().getName());
			}
			
			// local receiver corresponding to this chat room.
			IReceiver localReceiver = new Receiver(_localUserStub, localChatroomCopy);
			
			System.out.println("countChatroom:"+countChatroom);
			System.out.println("IReceiver.BOUND_PORT_CLIENT + countChatroom = " + RECEIVER_STUB_PORT + countChatroom);
			
			IReceiver localReceiverStub = (IReceiver) UnicastRemoteObject.exportObject(localReceiver, RECEIVER_STUB_PORT + countChatroom);
			((Receiver) localReceiver).setLocalReceiverStub(localReceiverStub);
			countChatroom++;
			
			IMiniMVCAdapter miniMVC = mainView.makeMiniMVC(localChatroomCopy, localReceiverStub);
			miniMVCs.put(localChatroomCopy.getUUID(), miniMVC);

			// set cmd for algo -> set algo for receiver
			/**
			 * Adapter that allows cmd to call model's methods.
			 */
			ICmd2ModelAdapter cmd2Model = new ICmd2ModelAdapterLocal() {

				@Override
				public void refreshChatterList() {
					miniMVC.refreshChatterList();
				}

				@Override
				public void appendMsg(String text, String name) {
					miniMVC.appendMsg(text);
				}

				@Override
				public void buildScrollableComponent(IComponentFactory fac, String label) {
					miniMVC.addScrollableComponentToTab(fac.makeComponent());
				}

				@Override
				public void buildNonScrollableComponent(IComponentFactory fac, String label) {
					
				}
			};
			
			((Receiver) localReceiver).setDataPacketAlgo((ICmd2ModelAdapterLocal) cmd2Model);
			
			_localUser.addChatRoom(localChatroomCopy);
			
			// set local receiver stub ? may be redundant as minimodel also has local receive stub.
			((ChatRoom) localChatroomCopy).setLocalIReceiverStub(localReceiverStub);
			//localChatroomCopy.addIReceiverStub(localReceiverStub);
			miniMVC.notifyJoin();
			miniMVC.refreshChatterList();			
			
		} catch (Exception e) {
			System.err.println("<<<ChatAppModel.joinChatroom()>>> Exception in joining chatroom: " + chatroomToBeJoined.getName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Quit all chat rooms and stop rmi server.
	 * May crash other connecting users as the API doesn't deal with unvalid IUser stub.
	 */
	public void stop() {
		// First use a list to hold chat rooms. 
		// if quiting chatroom ( during which chat room is removed from IUser's chatroom list) while traversing
		// User.getChatRooms(), there will be CONCURRENT EXCEPTION. 
		// --- you cannot modify an iterable while iterating it.
		List<IChatRoom> roomsToQuit = new LinkedList<IChatRoom>();
		try {
			for(IChatRoom room : _localUser.getChatRooms()) {
				roomsToQuit.add(room);
			}
		} catch (RemoteException e1) {
			System.out.println("<<<ChatAppModel.stop()>>> Exception getting chatrooms from localUser");
			e1.printStackTrace();
		}
		for (IChatRoom room : roomsToQuit) {
			quitChatRoom(room);
		}
		// stop the rmi server.
		try {
			_registry.unbind(IUser.BOUND_NAME);
			_rmiUtils.stopRMI();
			System.exit(0);
		} catch(Exception e) {
			System.err.println("[ChatAppModel.stop()] Exception logging out");
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/**
	 * get User Stub port
	 * @return user stub port.
	 */
	public int getUserStubPort() {
		return MAIN_STUB_PORT;
	}
	
	/**
	 * get rmi server port 
	 * @return rmi server port.
	 */
	public int getServerPort() {
		return RMI_SERVER_PORT;
	}
	
}
