package application.gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;


public class Controller implements Initializable {
	
	// Soyons Organisé
	
	// Tous les composants du PaneFind
	@FXML
	private Pane paneFind;
	
	@FXML
	private Button btnSearch;
	
	@FXML
	private ToggleButton btnPeriod;
	
	@FXML
	private TextField txtName;

	// Tous les composants du PaneDate (qui lui même est dans PaneSpecie)
	@FXML
	private Pane paneDate;
	
	@FXML
	private DatePicker firstDate;
	
	@FXML
	private DatePicker lastDate;
	
	@FXML 
	private Label lblFirstDate;
	
	@FXML
	private Label lblLastDate;
	
	// Tous les composants du PaneSpecie
	@FXML
	private Pane paneSpecie;
	
	@FXML	
	private TextField txtNameInfo;
	
	@FXML
	private TextField txtScientificNameInfo;
	
	@FXML
	private TextField txtSuperclassInfo;
	
	@FXML
	private TextField txtOrderInfo;
	
	// Tous les composants du paneListView
	@FXML
	private Pane paneListView;
	
	@FXML
	private ListView<String> listViewReport;
	
	@FXML
	private ListView<String> listViewSpecie;
	
	// Tous les composants du PaneCaption
	@FXML
	private Pane paneCaption;
	
	@FXML
	private Label lblCaption1;
	
	@FXML
	private Label lblCaption2;
	
	@FXML
	private Label lblCaption3;
	
	@FXML
	private Label lblCaption4;
	
	@FXML
	private Label lblCaption5;
	
	@FXML
	private Label lblCaption6;
	
	@FXML
	private Label lblCaption7;
	
	@FXML
	private Label lblCaption8;
	
	@FXML 
	private Circle circleCaption1;
	
	@FXML 
	private Circle circleCaption2;
	
	@FXML 
	private Circle circleCaption3;
	
	@FXML 
	private Circle circleCaption4;
	
	@FXML 
	private Circle circleCaption5;
	
	@FXML 
	private Circle circleCaption6;
	
	@FXML 
	private Circle circleCaption7;
	
	@FXML 
	private Circle circleCaption8;
	
	// Tous les composants du paneControl3D
	@FXML
	private ToggleButton btnHisto;
	
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
	
	@FXML 
	private Slider sliderMap;
	
	@FXML 
	private TextField txtZoom;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialisation de la visibilité des composants
		//setDateNotVisible();
		paneDate.setVisible(false);
		
		// Création d'un EventListener pour l'interaction avec btnPeriod
		btnPeriod.setOnAction(event ->  
    	{
    		if(btnPeriod.isSelected()) {
    			setDateVisible();
    		}
    		else {
    			setDateNotVisible();
    		}
        });
		
		// Création d'un EventListener pour l'intéraction avec le bouton Rechercher
		btnSearch.setOnAction(event ->
		{
			//do smth
		});
		
		
	}
	
	public void setDateVisible() {
		paneDate.setVisible(true);
	}
	
	public void setDateNotVisible() {
		paneDate.setVisible(false);
	}
}
