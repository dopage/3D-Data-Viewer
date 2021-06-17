package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("gui/View.fxml"));
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle("Projet IHM - Esteban & Rayane - OBIS 3D");
			//primaryStage.setResizable(false);
			primaryStage.setMinWidth(1220);
			primaryStage.setMinHeight(860);
			primaryStage.show();
			//Code pour connaitre les dimensions de la fenetre lors d'un resize
			/*
			primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
	        	System.out.println(newVal.doubleValue());
	        });
			primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
	        	System.out.println(newVal.doubleValue());
	        });
	        */
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
