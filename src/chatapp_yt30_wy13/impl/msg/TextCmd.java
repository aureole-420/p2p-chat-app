package chatapp_yt30_wy13.impl.msg;

import common.DataPacketAlgoCmd;
import common.DataPacketChatRoom;
import common.ICmd2ModelAdapter;

import java.util.Date;

/**
 * The cmd for text msg type.
 * @author yt30, wy13.
 */
public class TextCmd extends DataPacketAlgoCmd<TextMsg>{
	/**
	 * generated serial id.
	 */
	private static final long serialVersionUID = 5699844272613958625L;
	/**
	 * adapter that allows cmd to call model's method.s
	 */
	private transient ICmd2ModelAdapter cmd2ModelAdpt; 
	/**
	 * Constructor of TextCmd.
	 * @param cmd2MAdpt A cmd2Model adapter.
	 */
	public TextCmd(ICmd2ModelAdapter cmd2MAdpt) {
		this.cmd2ModelAdpt = cmd2MAdpt;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketChatRoom<TextMsg> msg, String... params) {
		try {
			Date time = new Date();
			String prefix = "\n[" + msg.getSender().getUserStub().getName()+ "]  " + time + "\n";
			cmd2ModelAdpt.appendMsg(prefix + msg.getData().getContent(), null);
		} catch (Exception e) {
			System.err.println("<<<TextCmd.apply()>>>: Exception displaying text on miniView " + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt0) {
		this.cmd2ModelAdpt = cmd2ModelAdpt0;
	}

}
