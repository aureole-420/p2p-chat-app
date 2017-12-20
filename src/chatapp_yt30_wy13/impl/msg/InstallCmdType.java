package chatapp_yt30_wy13.impl.msg;

import common.DataPacketAlgoCmd;
import common.IInstallCmdType;

/**
 * The implementation of the well-knowned IInstallCmdType.
 * @author yt30, wy13.
 */
public class InstallCmdType implements IInstallCmdType {
	
	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = -3740927154196044813L;
	
	/**
	 * The cmd to install.
	 */
	private DataPacketAlgoCmd<?> cmd;
	/**
	 * The cmd id.
	 */
	private Class<?> cmdId;
	
	/**
	 * Constructor of the InstallCmdType.
	 * @param cmdId0 a cmd id.
	 * @param cmd0 a cmd.
	 */
	public InstallCmdType(Class<?> cmdId0, DataPacketAlgoCmd<?> cmd0) {
		this.cmdId = cmdId0;
		this.cmd = cmd0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public DataPacketAlgoCmd<?> getCmd() {
		return cmd;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getCmdId() {
		return cmdId;
	}

}
