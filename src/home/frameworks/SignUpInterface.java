package home.frameworks;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SignUpInterface extends Stub {
	
	public boolean idAuthenticate(String inputID) throws FileNotFoundException;
	
	public boolean personAuthenticate(String inputNumber) throws FileNotFoundException;
	
	public void createUserData(String id);
}