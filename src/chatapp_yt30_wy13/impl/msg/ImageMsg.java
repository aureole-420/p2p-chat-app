package chatapp_yt30_wy13.impl.msg;



import javax.swing.ImageIcon;

/**
 * The data type for transmission image.
 * @author yt30, wy13.
 */
public class ImageMsg implements IMsg {

	private static final long serialVersionUID = 8070269015779432484L;
	/**
	 * The image icon to be transmitted.
	 */
	private ImageIcon img;
	
	/**
	 * constructor of image msg 
	 * @param img0 An instance of image icon.
	 */
	public ImageMsg(ImageIcon img0) {
		this.img = img0;
	}
	
	/**
	 * getter for imageicon.
	 * @return the imageicon.
	 */
	public ImageIcon getImage() {
		return img;
	}

}
