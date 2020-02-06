package home.frameworks;

import java.rmi.RemoteException;

import home.model.UserModel;

public interface UserInfoInterface extends Stub {
	
	public UserModel checkCurrentUser() throws RemoteException;
}
