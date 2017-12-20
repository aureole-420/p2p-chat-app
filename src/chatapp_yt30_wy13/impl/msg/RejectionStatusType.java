package chatapp_yt30_wy13.impl.msg;

import common.DataPacketChatRoom;
import common.IRejectionStatusType;

public class RejectionStatusType implements IRejectionStatusType{

	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = 346565198524132549L;
	/**
	 * Original data or the failed data packet.
	 */
	private DataPacketChatRoom<?> originalData;
	/**
	 * The failure information to be sent to the sender of original data packet.
	 */
	private String failureInfo;
	
	public RejectionStatusType(DataPacketChatRoom<?> originalData0, String failureInfo0) {
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
