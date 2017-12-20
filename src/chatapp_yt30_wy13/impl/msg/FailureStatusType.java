package chatapp_yt30_wy13.impl.msg;

import common.DataPacketChatRoom;
import common.IFailureStatusType;

/**
 * Implementation of IFailureStatusType
 * @author yt30, wy13.
 */
public class FailureStatusType implements IFailureStatusType {	
	/**
	 * Generated serial ID.
	 */
	private static final long serialVersionUID = -8872792099803322584L;
	/**
	 * Original data or the failed data packet.
	 */
	private DataPacketChatRoom<?> originalData;
	/**
	 * The failure information to be sent to the sender of original data packet.
	 */
	private String failureInfo;
	
	public FailureStatusType(DataPacketChatRoom<?> originalData0, String failureInfo0) {
		this.originalData = originalData0;
		this.failureInfo = failureInfo0;
	}
	
	@Override
	public DataPacketChatRoom<?> getOriginalData() {
		return originalData;
	}

	@Override
	public String getFailureInfo() {
		return failureInfo;
	}

}
