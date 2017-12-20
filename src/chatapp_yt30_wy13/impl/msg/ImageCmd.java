package chatapp_yt30_wy13.impl.msg;

import java.awt.Component;
import java.rmi.RemoteException;
import java.util.Date;

import javax.swing.JLabel;

import common.DataPacketAlgoCmd;
import common.DataPacketChatRoom;
import common.ICmd2ModelAdapter;
import common.IComponentFactory;

/**
 * Cmd to deal with datapacket of ImageMsg.
 * @author yt30, wy13
 */
public class ImageCmd extends DataPacketAlgoCmd<ImageMsg> {
	
	private static final long serialVersionUID = -4139436767489049104L;
	/**
	 * local adapter for model's methods.
	 */
	private transient ICmd2ModelAdapter cmd2Model; /// MUST BE TRANSCIENT!!!!
	
	/**
	 * Constructor for ImageCmd
	 * @param cmd2Model0 An instance of cmd2ModeAdapter.
	 */
	public ImageCmd(ICmd2ModelAdapter cmd2Model0) {
		this.cmd2Model = cmd2Model0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String apply(Class<?> index, DataPacketChatRoom<ImageMsg> msg, String... params) {
		Date time = new Date();
		String prefix;
		try {
			prefix = "\n[" + msg.getSender().getUserStub().getName()+ "]  " + time + "\n";
			cmd2Model.appendMsg(prefix, null);
		} catch (RemoteException e) {
			System.err.println("<<<ImageCmd.apply()>>> Exception calling remote method on IReceiver object " + msg.getSender());
			e.printStackTrace();
		}
		cmd2Model.buildScrollableComponent(new IComponentFactory() {
			JLabel lblImage = new JLabel(msg.getData().getImage());
			@Override
			public Component makeComponent() {
				return lblImage;
			}
		}, null);

		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setCmd2ModelAdpt(ICmd2ModelAdapter cmd2ModelAdpt) {
		this.cmd2Model = cmd2ModelAdpt;
		
	}

}
