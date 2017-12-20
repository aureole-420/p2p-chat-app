package chatapp_yt30_wy13.impl;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import chatapp_yt30_wy13.impl.msg.ExceptionStatusType;
import chatapp_yt30_wy13.impl.msg.ImageCmd;
import chatapp_yt30_wy13.impl.msg.ImageMsg;
import chatapp_yt30_wy13.impl.msg.InstallCmdType;
import chatapp_yt30_wy13.impl.msg.TextCmd;
import chatapp_yt30_wy13.impl.msg.TextMsg;
import common.IReceiver;
import common.IRejectionStatusType;
import common.IRemoveReceiverType;
import common.IRequestCmdType;
import common.DataPacketAlgoCmd;
import common.DataPacketChatRoom;
import common.IAddReceiverType;
import common.IChatRoom;
import common.ICmd2ModelAdapter;
import common.IExceptionStatusType;
import common.IFailureStatusType;
import common.IInstallCmdType;
import common.IUser;
import provided.datapacket.DataPacketAlgo;

/**
 * Implementation of IReceiver
 * @author yt30, wy13
 *
 */
public class Receiver implements IReceiver {
	/**
	 * The User stub associated with the receiver
	 */
	private IUser TheUserStub;
	/**
	 * The chatroom associated with the receiver
	 */
	private IChatRoom TheChatroom;
	/**
	 * The algorithm for the datapacket ---- notice the difference between _algo and _cmds. 
	 * Only cmds in algo can be used to decoding datapacket. cmds in _cmds can only be used when sending a datapacket.
	 * Literally a cmd in _cmds is unknown to the receiver until it is installed to _algo.
	 */
	private DataPacketAlgo<String, String> _algo = null;
	/**
	 * uuid of the receiver.
	 */
	private UUID uuid;
	/**
	 * the adapter allowing cmd to use model's method.
	 */
	private transient ICmd2ModelAdapter _cmd2ModelAdpt = null; // shared by all users;
	/**
	 * the receiver stub of this remote object.
	 */
	private IReceiver _localReceiverStub;
	/**
	 * This map is used to temporarily store datapacket of unknown datatype, so that they
	 * can be executed once the corresponding cmds are obtained.
	 */
	private HashMap<Class<?>, LinkedList<DataPacketChatRoom<?>>> _dataBuffer = new HashMap<>();
	/**
	 * The  cmds for the datapacket -- notice the difference from _algo.
	 */
	private HashMap<Class<?>, DataPacketAlgoCmd<?>> _cmds = new HashMap<>();
	
    /**
     * Constructor of receiver.
     * @param userStub The associated user stub
     * @param chatroom The associated chatroom.
     */
	public Receiver(IUser userStub, IChatRoom chatroom) {
		this.TheUserStub = userStub;
		this.TheChatroom = chatroom;
		uuid = UUID.randomUUID();
	}
	/**
	 * Setter for local receiver stub.
	 * @param r The associated local receiver stub 
	 */
	public void setLocalReceiverStub(IReceiver r) {
		_localReceiverStub = r;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public <D> void receive(DataPacketChatRoom<D> msg) throws RemoteException {
		// ((Chatroom) getChatroom()).displayMsg(Msg);
		try {
			msg.execute(_algo, "String parameter not used.");
		} catch (Exception e) {
			System.err.println("[Receiver.receive()] Exception executing datapacket: " + msg + " --- with algo ----" + _algo);
			e.printStackTrace();
			System.out.println(-1);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IUser getUserStub() throws RemoteException {
		return TheUserStub;
	}
	
	/**
	 * getter for associated chatroom 
	 * @return The associated chatroom.
	 */
	public IChatRoom getChatroom() {
		return TheChatroom;
	}

	/**
	 * setter for datapacket algo.
	 * @param adpt The ICmd2model adapter .
	 */
	public void setDataPacketAlgo(ICmd2ModelAdapterLocal adpt) {
		_cmd2ModelAdpt = adpt;
		
		// initialize _algo with default cmd.
		// notice that once default cmd is called, a request (in forms of datapacket) will be sent
		// asking for the corresponding cmd.
		_algo = new DataPacketAlgo<String, String> (new DataPacketAlgoCmd<Object>() { // default cmd
			private static final long serialVersionUID = -7485697026108832029L;

			@Override
			public String apply(Class<?> index, DataPacketChatRoom<Object> msg, String... params) {
				Object x = msg.getData();
				try {
					_cmd2ModelAdpt.appendMsg("---------- <<<Receiver>>> Your DEFAULT CMD is being called in user" + TheUserStub.getName() 
					+ " Unknown data type is ---- " + index + "----- from sender " + msg.getSender(), null);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
				_cmd2ModelAdpt.appendMsg(" ---------- <<<Receiver>>> Start storing datapacket:: " + x, null);
				// since the datatype index is unknown, the receiver must send an IRequest message to the sender to 
				// request the cmd;
				if (!_dataBuffer.containsKey(index)) {
					_dataBuffer.put(index, new LinkedList<DataPacketChatRoom<?>>());
				}
				_dataBuffer.get(index).add(msg);	
				_cmd2ModelAdpt.appendMsg("---------- <<<Receiver>>> the data packet is stored :: " + x, null);
				
				
				try {
					//_cmd2ModelAdpt.appendMsg("<<<Receiver>>> default cmd, msg.getSender() : " + msg.getSender(), null);
					//_cmd2ModelAdpt.appendMsg("<<<Receiver>>> localReceiverStub : " + _localReceiverStub, null);
					// send a request datapacket.
					 msg.getSender().receive(new DataPacketChatRoom<IRequestCmdType> (IRequestCmdType.class, 
						new RequestCmdType(index), _localReceiverStub));
					 
					_cmd2ModelAdpt.appendMsg("---------- <<<Receiver.DefaultCmd.IRequestCmdType>>> Msg sent to sender for cmd of unknown datapacket of type :: " + index, null);
				} catch (RemoteException e) {
					System.out.println("<<<Receiver.DefaultCmd.IRequestCmdType>>> exception making remote calls");
					e.printStackTrace();
				}
				
				return "Default case called. data = " + x; 
			}
			
			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {// do nothing for default cmd
			}
			});
		
		/**
		 * SIX WELL KNOWN DATA TYPE
		 * 1. IAddReceiverType  
		 * 2. IRemoveReceiverType
		 * 3. IRequestCmdType
		 * 4. IInstallCmdType
		 * 5. 
		 */
		// defining the cmd for IAddReceiverType.
		_algo.setCmd(IAddReceiverType.class, new DataPacketAlgoCmd<IAddReceiverType> () {
			private static final long serialVersionUID = 8060391944340906421L;
			
			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IAddReceiverType> msg, String... params) {
				IReceiver receiverStubToBeAdded = msg.getData().getReceiverStub();
				TheChatroom.addIReceiverStubLocally(receiverStubToBeAdded); // add receiver
				// call receiver's cmd2ModelAdpt to interact with miniView ///
				((ICmd2ModelAdapterLocal) _cmd2ModelAdpt).refreshChatterList();
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt0) {}
		});
		
		// defining cmd for IRemoveReceiverType
		_algo.setCmd(IRemoveReceiverType.class, new DataPacketAlgoCmd<IRemoveReceiverType> () {
			private static final long serialVersionUID = 8060391944340906421L;
			
			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IRemoveReceiverType> msg, String... params) {
				IReceiver receiverStubToBeRemoved = msg.getData().getReceiverStub();
				TheChatroom.removeIReceiverStubLocally(receiverStubToBeRemoved); // add receiver
				// call receiver's cmd2ModelAdpt to interact with miniView 
				((ICmd2ModelAdapterLocal) _cmd2ModelAdpt).refreshChatterList();
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt0) {}
		});
		
		/**
		 * defnining cmd for IIstallCmdType
		 */
		_algo.setCmd(IInstallCmdType.class, new DataPacketAlgoCmd<IInstallCmdType>() { // installCmdType
			private static final long serialVersionUID = -625247822563250103L;

			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IInstallCmdType> msg, String... params) {
				// install cmd
				Class<?> id = msg.getData().getCmdId();
				DataPacketAlgoCmd<?> new_cmd = msg.getData().getCmd();
				new_cmd.setCmd2ModelAdpt(_cmd2ModelAdpt); // reset cmd2ModelAdapter for the new cmd
				_algo.setCmd(id, new_cmd);
				// then dealing with data in the _databuffer
				for (DataPacketChatRoom<?> data : _dataBuffer.get(id)) {
					try {
						receive(data);
					} catch (RemoteException e) {
						System.out.println("<<<Receiver.installCmdTyp.cmd>>> remote exception in executing data.");
						String failureInfo = "Failured in dealing with Data Type :: " + index + "  with cmds sent from " + msg.getSender();
						try {
							msg.getSender().receive(new DataPacketChatRoom<IExceptionStatusType> (IExceptionStatusType.class, 
									new ExceptionStatusType(msg, failureInfo), _localReceiverStub));
						} catch (RemoteException e1) {
							e1.printStackTrace();
						}
						e.printStackTrace();
					}
				}
				_dataBuffer.remove(id);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
			}
		});
		
		// defining cmd for IRequestCmdType
		_algo.setCmd(IRequestCmdType.class, new DataPacketAlgoCmd<IRequestCmdType>() {
			private static final long serialVersionUID = -4883714237760312564L;

			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IRequestCmdType> msg, String... params) {
				Class<?> idOfUnknownType = msg.getData().getCmdId();
				// sender of IRequestCmdType msg, A,  is the one who wish to get that cmd of T.
				// receiver of IRequestCmdTypeMsg, B,  is the one who send a datapacket of type T (unknown to A).
				_cmd2ModelAdpt.appendMsg("---------- <<<Receiver.IRequestCmdType.cmd>>> algo.cmd(IRequestCmdType) id of unknown type :: " + idOfUnknownType, null);
				
				try {
					msg.getSender().receive(new DataPacketChatRoom<IInstallCmdType> (IInstallCmdType.class, 
							new InstallCmdType(idOfUnknownType, _cmds.get(idOfUnknownType)), _localReceiverStub)); // one may use Receiver.this to replace _localReceiverStub
				} catch (RemoteException e) {
					System.out.println("<<<Receiver.IRequestCmdType.cmd>>> exception making remote calls");
					e.printStackTrace();
				} catch (Exception e) {
					_cmd2ModelAdpt.appendMsg("---------- <<<Receiver.IRequestCmdType.cmd>>> excpetion carrying out cmd.", null);
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
			}
		});
		
		_algo.setCmd(IFailureStatusType.class, new DataPacketAlgoCmd<IFailureStatusType>() {
			private static final long serialVersionUID = -8141273785087550268L;
			
			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IFailureStatusType> host, String... params) {
				_cmd2ModelAdpt.appendMsg("[Failure encountered] -- " + host.getData().getOriginalData(), null);
				_cmd2ModelAdpt.appendMsg("[Failure info] ::" + host.getData().getFailureInfo(), null);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
			}	
		});
		
		_algo.setCmd(IRejectionStatusType.class, new DataPacketAlgoCmd<IRejectionStatusType>() {
			private static final long serialVersionUID = -8141273785087550268L;
			
			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IRejectionStatusType> host, String... params) {
				_cmd2ModelAdpt.appendMsg("[Rejection failure encountered] -- " + host.getData().getOriginalData(), null);
				_cmd2ModelAdpt.appendMsg("[Rejection Failure info] ::" + host.getData().getFailureInfo(), null);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
			}	
		});
		
		_algo.setCmd(IExceptionStatusType.class, new DataPacketAlgoCmd<IExceptionStatusType>() {
			private static final long serialVersionUID = -8141273785087550268L;
			
			@Override
			public String apply(Class<?> index, DataPacketChatRoom<IExceptionStatusType> host, String... params) {
				_cmd2ModelAdpt.appendMsg("[Exception failure encountered] -- " + host.getData().getOriginalData(), null);
				_cmd2ModelAdpt.appendMsg("[Exception Failure info] ::" + host.getData().getFailureInfo(), null);
				return null;
			}

			@Override
			public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
			}	
		});
		//algo.setCmd();
		
		//_algo.setCmd(TextMsg.class, new TextCmd(_cmd2ModelAdpt));
		
		//_algo.setCmd(ImageMsg.class, new ImageCmd(_cmd2ModelAdpt));
		_cmds.put(TextMsg.class, new TextCmd(_cmd2ModelAdpt));
		_cmds.put(ImageMsg.class, new ImageCmd(_cmd2ModelAdpt));
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getUUID() throws RemoteException{
		return uuid;
	}

}
