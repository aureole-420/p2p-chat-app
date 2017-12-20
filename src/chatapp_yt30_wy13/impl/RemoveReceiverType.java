package chatapp_yt30_wy13.impl;

import common.IReceiver;
import common.IRemoveReceiverType;

/**
 * Implementation of IRemoveReceiverTyp
 * @author yt30, wy13
 */
public class RemoveReceiverType implements IRemoveReceiverType {
	/**
	 * generated serial id.
	 */
	private static final long serialVersionUID = 6748118633005081409L;
	/**
	 * The reciever stub to remove.
	 */
	private IReceiver receiverStubToBeRemoved;
	
	/**
	 * The constructor of removereceiverType.
	 * @param r The receiver stub to remove
	 */
	public RemoveReceiverType(IReceiver r) {
		this.receiverStubToBeRemoved = r;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public IReceiver getReceiverStub() {
		return receiverStubToBeRemoved;
	}

}
