package chatapp_yt30_wy13.impl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import common.IChatRoom;
import common.IUser;

/**
 * Decoration class for IUser so that toString() method can be rewritten.
 * @author yt30, wy13.
 */
public class ProxyUser implements IUser, Serializable {
	/**
	 * Generated serial id.
	 */
	private static final long serialVersionUID = -3031780245297156300L;
	/**
	 * User stub to be decorated.
	 */
	private IUser stub;
	
	/**
	 * Constructor of the proxy user.
	 * @param stub User stub to be decorated.
	 */
	public ProxyUser(IUser stub) {
		this.stub = stub;
	}
	
	/**
	 * Getter for the IUser object.
	 * @return The IUser object.
	 */
	public IUser getStub() {
		return stub;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public UUID getUUID() throws RemoteException {
		return stub.getUUID();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() throws RemoteException {
		return stub.getName();
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect(IUser userStub) throws RemoteException {
		stub.connect(userStub);	
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof IUser) {
			try {
				return stub.getUUID().equals(((IUser) other).getUUID());
			} catch (RemoteException e) {
				// Deal with the exception without throwing a RemoteException.
				System.err.println("ProxyUser.equals(): error getting UUID: "+ e);
				e.printStackTrace();
				// Fall through and return false
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<IChatRoom> getChatRooms() throws RemoteException {
		try {return stub.getChatRooms();}
		catch(RemoteException e) {
			System.out.println("&&&&&&&&&&&&");
			e.printStackTrace();
			throw new RemoteException();
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		try {
			return stub.getUUID().hashCode();
		} catch (RemoteException e) {
			System.err.println("ProxyStub.hashCode(): Error calling remote method on IUser stub: "+e);
			e.printStackTrace();
			return super.hashCode();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		try {
			return stub.getName();
		} catch (RemoteException e) {
			System.err.println("ProxyStub.toString(): Error calling remote method on IUser stub: " + e);
			e.printStackTrace();
			return super.toString();
		}
	}


}
