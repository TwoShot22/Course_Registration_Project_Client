package home.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

import home.Connector;
import home.frameworks.CheckDuplicationInterface;
import home.frameworks.Invoker;
import home.frameworks.LoginInterface;
import home.frameworks.SignUpInterface;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController implements Initializable {
	private static final long serialVersionUID = 1L;

	private MainController controller;
	
	@FXML Button registerButton;
	@FXML Button cancelButton;
	
	@FXML TextField idTextField;
	@FXML PasswordField passwordField;
	@FXML TextField nameTextField;
	@FXML TextField collegeTextField;
	@FXML TextField departmentTextField;
	@FXML TextField numberTextField;
	
	private Boolean signUpCheck = false;
	// 사용자 입력 Data
	private String inputID;
	private String inputPassword;
	private String inputName;
	private String inputCollege;
	private String inputDepartment;
	private String inputNumber;
	
	// DB에 있는 기존 정보
	private String dataID;
	private String dataPassword;
	private String dataName;
	private String dataCollege;
	private String dataDepartment;
	private String dataNumber;
	
	// frameworks
	private static final Class<SignUpInterface> SU_CLASS = SignUpInterface.class;
	private static final Class<CheckDuplicationInterface> CD_CLASS = CheckDuplicationInterface.class;
	
	private static Method createUserDataMethod;
	private static Method manageUserFileMethod;
	private static Method idAuthenticateMethod;
	private static Method personAuthenticateMethod;
	
	public SignUpController() {
		this.controller = new MainController();
	}
	
	static {
		try {
			createUserDataMethod = SU_CLASS.getMethod("createUserData", String.class);
			manageUserFileMethod = CD_CLASS.getMethod("manageUserFile", String.class, String.class);
			idAuthenticateMethod = SU_CLASS.getMethod("idAuthenticate", String.class);
			personAuthenticateMethod = SU_CLASS.getMethod("personAuthenticate", String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		registerButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event) {
				handleRegisterButtonAction(event);
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				handleCancelButtonAction(event);
			}
		});
	}
	
	public void handleRegisterButtonAction(ActionEvent event) {
		this.signUpProcess();
		
		if(this.signUpCheck) {
			Stage signUp = (Stage)registerButton.getScene().getWindow();
			signUp.close();
		} else {
			idTextField.setText(null);
			passwordField.setText(null);
			nameTextField.setText(null);
			collegeTextField.setText(null);
			departmentTextField.setText(null);
			numberTextField.setText(null);
		}
	}
	
	public void handleCancelButtonAction(ActionEvent event) {
		Stage signUp = (Stage)cancelButton.getScene().getWindow();
		signUp.close();
	}
	
	private void signUpProcess() {
		boolean idCheck;
		boolean numberCheck;
		
		this.inputID = idTextField.getText();
		this.inputPassword = passwordField.getText();
		this.inputName = nameTextField.getText();
		this.inputCollege = collegeTextField.getText();
		this.inputDepartment = departmentTextField.getText();
		this.inputNumber = numberTextField.getText();
		
		try {
			idCheck = this.idAuthenticate(this.inputID);
			numberCheck = this.personAuthenticate(this.inputNumber);
			
			if(!idCheck && !numberCheck) {
				String userInfo = this.inputID+" "+this.inputPassword+" "+this.inputName+" "+this.inputCollege + " "+this.inputDepartment + " "+this.inputNumber;
				Connector.invoke(new Invoker(CD_CLASS.getSimpleName(), manageUserFileMethod.getName(), manageUserFileMethod.getParameterTypes(), new Object[] {userInfo, "data/User/Login"}));
				this.signUpCheck = true;
				
				Connector.invoke(new Invoker(SU_CLASS.getSimpleName(), createUserDataMethod.getName(), createUserDataMethod.getParameterTypes(), new Object[] {inputID}));
				
			} else if(!idCheck && numberCheck) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Sign Up Failed");
				alert.setHeaderText("Only One ID allowed for One Person");
				alert.setContentText("Please Try Again");
				alert.show();
				this.signUpCheck = false;
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Sign Up Failed");
				alert.setHeaderText("There is Duplicated ID");
				alert.setContentText("Please Try Again");
				alert.show();
				this.signUpCheck = false;
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean idAuthenticate(String inputID) throws FileNotFoundException {
		try {
			return (boolean) Connector.invoke(new Invoker(SU_CLASS.getSimpleName(), idAuthenticateMethod.getName(), idAuthenticateMethod.getParameterTypes(), new Object[] {inputID}));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean personAuthenticate(String inputNumber) throws FileNotFoundException {
		try {
			return (boolean) Connector.invoke(new Invoker(SU_CLASS.getSimpleName(), personAuthenticateMethod.getName(), personAuthenticateMethod.getParameterTypes(), new Object[] {inputNumber}));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}