package chatapp_yt30_wy13.impl.msg;

/**
 * The self-defined text msg type for transmitting string..
 * @author yt30, wy13.
 *
 */
public class TextMsg implements IMsg{
	
	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = 3617057226144391348L;
	/**
	 * String contained in the TextMsg.
	 */
	private String text;
	
	/**
	 * Constructor of text msg
	 * @param t A string of msg.
	 */
	public TextMsg(String t) {
		this.text = t;
	}
	
	/**
	 * getter for the content of the msg.
	 * @return The string to be transmitted.
	 */
	public String getContent() {
		return text;
	}
}
