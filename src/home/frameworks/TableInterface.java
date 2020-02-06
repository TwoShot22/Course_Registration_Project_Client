package home.frameworks;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.util.Vector;

import home.model.DirectoryModel;
import home.model.LectureModel;
import home.model.UserModel;

public interface TableInterface extends Stub {
	
    public UserModel checkCurrentUser();
    // Directory Methods

    // [Campus] File Read and Add Items
    public Vector<DirectoryModel> getCampusData(String fileName) throws FileNotFoundException;

    // [College] File Read and Add Items
    public Vector<DirectoryModel> getCollegeData(String fileName) throws FileNotFoundException;
    
    // [Department] File Read and Add Items
    public Vector<DirectoryModel> getDepartmentData(String fileName) throws FileNotFoundException;

    // Lecture Methods
    public Vector<LectureModel> getLectureData(String fileName) throws FileNotFoundException;
}