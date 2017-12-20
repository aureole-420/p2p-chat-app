package chatapp_yt30_wy13.impl;

import common.IRequestCmdType;

/**
 * Implementation of IRequestCmdType
 * @author yt30, wy13
 */
public class RequestCmdType implements IRequestCmdType {
	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = -3135458394446722192L;
	/**
	 * the cmd id.
	 */
	private Class<?> cmdId;
	
	/**
	 * Constructor of cmd id.
	 * @param cmdId0 the index of requested cmd.
	 */
	public RequestCmdType(Class<?> cmdId0) {
		this.cmdId = cmdId0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getCmdId() {
		return cmdId;
	}

}
