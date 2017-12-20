package chatapp_yt30_wy13.impl;

import common.ICmd2ModelAdapter;

/**
 * local extension of ICmd2ModelAdapter interface.
 * @author yt30, wy13.
 */
public interface ICmd2ModelAdapterLocal extends ICmd2ModelAdapter {
	
	/**
	 * refresh the chatter list in mini view.
	 */
	public void refreshChatterList();
}
