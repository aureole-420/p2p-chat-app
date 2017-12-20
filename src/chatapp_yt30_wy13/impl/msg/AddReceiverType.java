package chatapp_yt30_wy13.impl.msg;

import common.IAddReceiverType;
import common.IReceiver;

/**
 * The implementation of the well-known IAddReceiverType.
 * @author yt30, wy13
 */
public class AddReceiverType implements IAddReceiverType {

	private static final long serialVersionUID = -7084486855735218124L;
	// doesn't need to be transient because this msg type is well known; 
	// any user are supposed to have cmd for this msg type, so corresponding cmd does not need to be tranmitted.
	/**
	 * receiver stub to be added.
	 */
	private IReceiver ReceiverStubToBeAdded; 
	
	/**
	 * Constructor of the AddReceiverType.
	 * @param r receiver stub to be added.
	 */
	public AddReceiverType(IReceiver r) {
		this.ReceiverStubToBeAdded = r;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReceiver getReceiverStub() {
		return ReceiverStubToBeAdded;
	}

}
