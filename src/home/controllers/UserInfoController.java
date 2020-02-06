package home.controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

import home.Connector;
import home.frameworks.Invoker;
import home.frameworks.UserInfoInterface;
import home.model.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class UserInfoController implements Initializable{
	private static final long serialVersionUID = 1L;

	private MainController controller;
	
	@FXML Button closeWindowButton;
	
	@FXML Label nameLabel;
	@FXML Label numberLabel;
	@FXML Label collegeLabel;
	@FXML Label departmentLabel;	
	
	@FXML Label maxCreditLabel;
	@FXML Label currentCreditLabel;
	@FXML Label currentLectureLabel;
	
	// Current User Data
	private String userID;
	private String userName;
	private String userCollege;
	private String userDepartment;
	private String userNumber;
	
	// UserInfoControl
		
	public UserInfoController() {
		this.controller = new MainController();
		
		this.checkCurrentUser();
	}
	
	// frameworks
	private static final Class<UserInfoInterface> UI_CLASS = UserInfoInterface.class;
	
	private static Method checkCurrentUserMethod;
	
	static {
		try {
			checkCurrentUserMethod = UI_CLASS.getMethod("checkCurrentUser");
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void checkCurrentUser() {
		UserModel user;
		try {
			user = (UserModel) Connector.invoke(new Invoker(UI_CLASS.getSimpleName(), checkCurrentUserMethod.getName(), checkCurrentUserMethod.getParameterTypes(), new Object[] {}));
			userID = user.getUserID();
			userName = user.getUserName();
			userCollege = user.getUserCollege();
			userDepartment = user.getUserDepartment();
			userNumber = user.getUserNumber();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		closeWindowButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handleCloseWindowButton(event);
			}
		});
		
		nameLabel.setText("Name : "+this.userName);
		numberLabel.setText("Number : "+this.userNumber);
		collegeLabel.setText("College : "+this.userCollege);
		departmentLabel.setText("Dept. : "+this.userDepartment);
	}
	
	private void handleCloseWindowButton(ActionEvent event) {
		Stage userInfo = (Stage)closeWindowButton.getScene().getWindow();
		userInfo.close();
	}
}
