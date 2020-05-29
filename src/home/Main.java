package home;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		Parent login = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
        primaryStage.setTitle("�������б� ������û �ý���");
        primaryStage.getIcons().add(new Image("images/Myongji_Logo.gif"));
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
	}

	public static void main(String[] args) {
		try {
			Connector.connect();
			launch(args);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
