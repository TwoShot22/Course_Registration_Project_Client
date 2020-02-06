package home.controllers;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Vector;

import home.Connector;
import home.frameworks.CheckDuplicationInterface;
import home.frameworks.FileToolInterface;
import home.frameworks.Invoker;
import home.frameworks.TableInterface;
import home.model.DirectoryModel;
import home.model.LectureModel;
import home.model.UserModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class TableController implements Initializable{
	private static final long serialVersionUID = 1L;

    // Directory
    @FXML ComboBox<String> campusPickBox;
    @FXML ComboBox<String> collegePickBox;
    @FXML ComboBox<String> departmentPickBox;

    private Vector<DirectoryModel> campusModels;
    private ObservableList<String> campusItems;
    private ObservableList<String> campusList;

    private Vector<DirectoryModel> collegeModels;
    private ObservableList<String> collegeItems;
    private ObservableList<String> collegeList;

    private Vector<DirectoryModel> departmentModels;
    private ObservableList<String> departmentItems;
    private ObservableList<String> departmentList;

    private String startPath = "root";
    private String campusPath = "yongin";
    private String collegePath = "generalY";
    private String departmentPath = "englishYG";

    // Lecture
    @FXML TableView<LectureController> lectureTable;

    @FXML TableColumn<LectureController, Integer> numberColumn;
    @FXML TableColumn<LectureController, String> nameColumn;
    @FXML TableColumn<LectureController, String> professorColumn;
    @FXML TableColumn<LectureController, Integer> creditColumn;
    @FXML TableColumn<LectureController, String> timeColumn;

    private Vector<LectureController> lectureModels;
    ObservableList<LectureController> lectureList = FXCollections.observableArrayList();

    private Object oldValue;

    // Current User Data
    private String userID;
    private String userName;
    private String userCollege;
    private String userDepartment;
    private String userNumber;

    // Button
    @FXML Button lectureToBasket;
    @FXML Button lectureRefresh;

    @FXML Button basketMove;
    @FXML Button userMove;
    @FXML Button loginMove;

    @FXML Label userNotification;

    // Load Basket.fxml
    private MainController controller;
    
    private static final Class<TableInterface> TI_CLASS = TableInterface.class;
	private static final Class<CheckDuplicationInterface> CD_CLASS = CheckDuplicationInterface.class;

    private static Method checkCurrentUserMethod;
    private static Method getCampusDataMethod;
    private static Method getCollegeDataMethod;
    private static Method getDepartmentDataMethod;
    private static Method getLectureDataMethod;
    private static Method manageLectureFileMethod;
    
    
    static {
    	try {
			checkCurrentUserMethod = TI_CLASS.getMethod("checkCurrentUser");
			getCampusDataMethod = TI_CLASS.getMethod("getCampusData", String.class);
			getCollegeDataMethod = TI_CLASS.getMethod("getCollegeData", String.class);
			getDepartmentDataMethod = TI_CLASS.getMethod("getDepartmentData",String.class);
			getLectureDataMethod = TI_CLASS.getMethod("getLectureData",String.class);
			manageLectureFileMethod = CD_CLASS.getMethod("manageLectureFile", String.class, String.class, String.class);
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    // TableControl
    public TableController() {	
        this.controller = new MainController();
		
        this.checkCurrentUser();
    }

    public void checkCurrentUser() {
        UserModel user;
		try {
			user = (UserModel) Connector.invoke(new Invoker(TI_CLASS.getSimpleName(),checkCurrentUserMethod.getName(),checkCurrentUserMethod.getParameterTypes(), new Object[] {}));
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

    // Initialize Methods
    public void initialize(URL location, ResourceBundle resources) {
        userNotification.setText(this.userName+"님, 반갑습니다:)");

        // Directory Part
        campusItems = FXCollections.observableArrayList();
        campusList = FXCollections.observableArrayList();

        collegeItems = FXCollections.observableArrayList();
        collegeList = FXCollections.observableArrayList();

        departmentItems = FXCollections.observableArrayList();
        departmentList = FXCollections.observableArrayList();


        // Directory Method
        try {
			this.refreshDirectory();
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        campusList = campusItems;
        campusPickBox.setItems(campusList);
        campusPickBox.getSelectionModel().select(0);
        campusPickBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number campusValue) {
                try {
                    campusRefresh(campusValue.intValue());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        collegeList = collegeItems;
        collegePickBox.setItems(collegeList);
        collegePickBox.getSelectionModel().select(0);
        collegePickBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number collegeValue) {
                try {
                    collegeRefresh(collegeValue.intValue());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        departmentList = departmentItems;
        departmentPickBox.setItems(departmentList);
        departmentPickBox.getSelectionModel().select(0);
        departmentPickBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number departmentValue) {
                try {
                    departmentRefresh(departmentValue.intValue());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // Lecture Part
        lectureTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        numberColumn.setCellValueFactory(cellData->cellData.getValue().numberProperty().asObject());
        nameColumn.setCellValueFactory(cellData->cellData.getValue().nameProperty());
        professorColumn.setCellValueFactory(cellData->cellData.getValue().professorProperty());
        creditColumn.setCellValueFactory(cellData->cellData.getValue().creditProperty().asObject());
        timeColumn.setCellValueFactory(cellData->cellData.getValue().timeProperty());

        lectureTable.setOnMouseClicked(event->{
            if(lectureTable.getSelectionModel().getSelectedItem()!=null) {
                if(event.getPickResult().getIntersectedNode().equals(oldValue)) {
                    lectureTable.getSelectionModel().clearSelection();
                    oldValue = null;
                } else {
                    oldValue = event.getPickResult().getIntersectedNode();
                }
            }
        });

        // 상단 Progress Bar
        lectureToBasket.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Vector<String> selectedLecture = new Vector<>();
                Vector<String> selectedLectures = new Vector<>();
                ObservableList<LectureController> selectedItem = lectureTable.getSelectionModel().getSelectedItems();

                for(int i=0;i<selectedItem.size();i++) {
                    selectedLecture.add(String.valueOf(selectedItem.get(i).getNumber()));
                    selectedLecture.add(selectedItem.get(i).getName());
                    selectedLecture.add(selectedItem.get(i).getProfessor());
                    selectedLecture.add(String.valueOf(selectedItem.get(i).getCredit()));
                    selectedLecture.add(selectedItem.get(i).getTime());
                }

                for(int i=0;i<selectedLecture.size();i+=5) {
                    selectedLectures.add(selectedLecture.get(i)+" "+selectedLecture.get(i+1)+" "+selectedLecture.get(i+2)+" "+selectedLecture.get(i+3)+" "+selectedLecture.get(i+4));
                }

                String lectureMessage = "";
                if(selectedLectures.size()==0) {
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Occured");
                    alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nNo Allowed");
                    alert.setContentText("Please Select Lecture you want to move to Basket!");
                    alert.show();
                } else {
                    Alert alert = new Alert(AlertType.CONFIRMATION);
                    alert.setTitle("Confirm Sending Lecture");
                    alert.setHeaderText("You have selected [ "+selectedLectures.size()+" ] Lectures.\nAre you sure you want to put Lectures in Basket?");
                    for(int i=0;i<selectedLectures.size();i++) {
                        lectureMessage+=(selectedLectures.get(i)+"\n");
                    }
                    alert.setContentText(lectureMessage);

                    Optional<ButtonType> result = alert.showAndWait();
                    if(result.get()==ButtonType.OK) {
                        for(int i=0; i<selectedLectures.size();i++) {
                        	try {
								Connector.invoke(new Invoker(CD_CLASS.getSimpleName(),manageLectureFileMethod.getName(), manageLectureFileMethod.getParameterTypes(),new Object[] {selectedLectures.get(i),"data/user/"+userID+"_Basket","AddLecture"}));
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        }

                        Alert success = new Alert(AlertType.INFORMATION);
                        success.setTitle("Result Notification");
                        success.setHeaderText("Success to Send Lectures in Basket");
                        success.setContentText("성공");
                        success.show();
                    } else {
                        Alert cancel = new Alert(AlertType.ERROR);
                        cancel.setTitle("Result Notification");
                        cancel.setHeaderText("Fail to Send Lectures in Basket");
                        cancel.setContentText("실패");
                        cancel.show();
                    }
                }
            }
        });

        lectureRefresh.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                lectureTable.getSelectionModel().clearSelection();
            }
        });

        // Control Bar
        basketMove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO Auto-generated method stub
                handleBasketMoveAction(event);
            }
        });

        userMove.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                handleUserMoveAction(event);
            }
        });

        loginMove.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                handleLoginMoveAction(event);
            }
        });
    }

    // Directory Methods
    private void refreshDirectory() throws FileNotFoundException {
        this.campusPath = getCampusHyperLink(this.startPath);
        this.campusRefresh(0);

        this.collegePath = getCollegeHyperLink(this.campusPath);
        this.collegeRefresh(0);

        this.departmentPath = getDepartmentHyperLink(this.collegePath);
        this.departmentRefresh(0);
    }

    // [Campus] File Read and Add Items
    private Vector<DirectoryModel> getCampusData(String fileName) throws FileNotFoundException {
        try {
			return (Vector<DirectoryModel>) Connector.invoke(new Invoker(TI_CLASS.getSimpleName(), getCampusDataMethod.getName(), getCampusDataMethod.getParameterTypes(), new Object[] {fileName}));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    private String getCampusHyperLink(String fileName) throws FileNotFoundException {
        campusModels = getCampusData("data/"+fileName);

        campusItems.clear();

        for(DirectoryModel campusModel: campusModels) {
            campusItems.add(campusModel.getName());
        }

        return campusModels.get(0).getHyperLink();
    }

    private void campusRefresh(Object source) throws FileNotFoundException {
        int item = (int)source;
        this.campusPath = this.campusModels.get(item).getHyperLink();

		this.getCollegeHyperLink(this.campusPath);
        this.collegeRefresh(0);
        this.collegePickBox.getSelectionModel().select(0);
        this.departmentRefresh(0);
        this.departmentPickBox.getSelectionModel().select(0);
    }

    // [College] File Read and Add Items
    private Vector<DirectoryModel> getCollegeData(String fileName) throws FileNotFoundException {
        try {
			return (Vector<DirectoryModel>) Connector.invoke(new Invoker(TI_CLASS.getSimpleName(), getCollegeDataMethod.getName(), getCollegeDataMethod.getParameterTypes(), new Object[] {fileName}));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    private String getCollegeHyperLink(String fileName) throws FileNotFoundException {
        collegeModels = getCollegeData("data/"+fileName);

        collegeItems.clear();

        for(DirectoryModel collegeModel: collegeModels) {
            collegeItems.add(collegeModel.getName());
        }

        return collegeModels.get(0).getHyperLink();
    }

    private void collegeRefresh(Object source) throws FileNotFoundException {
        int item = (int)source;
        if(item<0) return;
        this.collegePath = this.collegeModels.get(item).getHyperLink();

        this.getDepartmentHyperLink(this.collegePath);
        this.departmentRefresh(0);
        this.departmentPickBox.getSelectionModel().select(0);
    }

    // [Department] File Read and Add Items
    private Vector<DirectoryModel> getDepartmentData(String fileName) throws FileNotFoundException {
        try {
			return (Vector<DirectoryModel>) Connector.invoke(new Invoker(TI_CLASS.getSimpleName(), getDepartmentDataMethod.getName(), getDepartmentDataMethod.getParameterTypes(), new Object[] {fileName}));
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    }

    private String getDepartmentHyperLink(String fileName) throws FileNotFoundException {
        departmentModels = getDepartmentData("data/"+fileName);

        departmentItems.clear();

        for(DirectoryModel departmentModel: departmentModels) {
            departmentItems.add(departmentModel.getName());
        }

        return departmentModels.get(0).getHyperLink();
    }

    private void departmentRefresh(Object source) throws FileNotFoundException {
        int item = (int)source;
        if(item<0) return;
        this.departmentPath = this.departmentModels.get(item).getHyperLink();
		this.getLectureList(this.departmentPath);
    }

    // Lecture Methods

    private Vector<LectureController> getLectureData(String fileName) throws FileNotFoundException{
    	Vector<LectureModel> lectureModels;
		try {
			lectureModels = (Vector<LectureModel>) Connector.invoke(new Invoker(TI_CLASS.getSimpleName(), getLectureDataMethod.getName(), getLectureDataMethod.getParameterTypes(), new Object[] {fileName}));
			Vector<LectureController> lectureControllers = new Vector<LectureController>(); // 실제 표현데이터 형식
			LectureController lectureController;
			
			for(LectureModel lectureModel: lectureModels) {
				lectureController = new LectureController();
				
				lectureController.setNumber(lectureModel.getNumber());
				lectureController.setName(lectureModel.getName());
				lectureController.setProfessor(lectureModel.getProfessor());
				lectureController.setCredit(lectureModel.getCredit());
				lectureController.setTime(lectureModel.getTime());
				
				lectureControllers.add(lectureController);
			}
			return lectureControllers;
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} // 파일에서 과목 정보 가져오기
    }

    private void getLectureList(String fileName) throws FileNotFoundException {
        lectureTable.getItems().clear();

        lectureModels = getLectureData("data/"+fileName);
        lectureList.clear();

        for(LectureController lectureModel: lectureModels) {
            lectureTable.getItems().add(new LectureController(new SimpleIntegerProperty(lectureModel.getNumber()), new SimpleStringProperty(lectureModel.getName()),
                    new SimpleStringProperty(lectureModel.getProfessor()), new SimpleIntegerProperty(lectureModel.getCredit()), new SimpleStringProperty(lectureModel.getTime())));
        }
    }

    // Control Bar Method
    public void handleBasketMoveAction(ActionEvent event) {
        this.controller.loadStage("src/home/fxml/Basket.fxml", "명지대학교 수강신청 시스템");
        Stage lecture = (Stage)basketMove.getScene().getWindow();
        lecture.close();
    }

    public void handleUserMoveAction(ActionEvent event) {
        this.controller.loadStage("src/home/fxml/UserInfo.fxml", "명지대학교 수강신청 시스템");
    }

    public void handleLoginMoveAction(ActionEvent event) {
        this.controller.loadStage("src/home/fxml/Login.fxml", "명지대학교 수강신청 시스템");
        Stage lecture = (Stage)loginMove.getScene().getWindow();
        lecture.close();
    }
}