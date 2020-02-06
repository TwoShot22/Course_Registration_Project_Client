package home.frameworks;

import java.io.FileNotFoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import home.model.UserModel;

public interface LoginInterface extends Stub{
	
	public UserModel getUser();
	// Login DB와 사용자 입력값 비교 메소드
	public boolean authenticate(String inputID, String inputPassword) throws FileNotFoundException;
		
}