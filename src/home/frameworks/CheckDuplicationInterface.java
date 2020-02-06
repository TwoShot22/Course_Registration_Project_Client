package home.frameworks;

import java.rmi.RemoteException;

public interface CheckDuplicationInterface extends Stub {

	public boolean checkOverLapData(String str1, String str2);
	public void manageLectureFile(String selectedLectures, String targetFileName, String mode);
	public void manageUserFile(String userInfo, String TargetFileName);
	public void manageCurrentUser(String userInfo, String TargetFileName);
}
