package application.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;


public class Controller implements Initializable {
	
	// Soyons Organisé
	
	// Tous les composants du PaneFind
	@FXML
	private Button btnSearch;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private DatePicker firstDate;
	
	@FXML
	private DatePicker lastDate;
	
	
	
	@FXML
	private Button btnPeriod;
	
	@FXML
	private Button btnHisto;
	
	@FXML
	private Button btnZoomMinus;
	
	@FXML
	private Button btnZoomPlus;
	
	@FXML
	private Button btnPlay;
	
	@FXML
	private Button btnPause;
	
	@FXML
	private Button btnStop;
	
	// 
	
	public Controller() {
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
}
