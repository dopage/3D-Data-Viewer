package application.gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
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
	
	// Tous les composants du Pane3D
	@FXML
	private Pane pane3D;
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// Initialisation de la visibilité des composants
		//setDateNotVisible();
		paneDate.setVisible(false);
		btnSearch.setDisable(true);
		
		txtNameInfo.setDisable(true);
		txtScientificNameInfo.setDisable(true);
		txtSuperclassInfo.setDisable(true);
		txtOrderInfo.setDisable(true);
		
		// La map monde est si belle
		Group root3D = new Group();
		Pane pane3D = new Pane(root3D);
		
		ObjModelImporter objImporter = new ObjModelImporter();
		
		try {
        	URL modelUrl = this.getClass().getResource("Earth/earth.obj");
        	System.out.println(modelUrl);
        	objImporter.read(modelUrl);
        } catch (ImportException e) {
        	System.out.println(e.getMessage());
        }
		
		MeshView[] meshViews = objImporter.getImport();
		Group earth = new Group(meshViews);
		root3D.getChildren().add(earth);
		
		// Ajout du group Camera
		PerspectiveCamera camera = new PerspectiveCamera(true);
		CameraManager camManager = new CameraManager(camera, pane3D, root3D);
		
		// Les lights
		PointLight light = new PointLight(Color.WHITE);
        light.setTranslateX(-180);
        light.setTranslateY(-90);
        light.setTranslateZ(-120);
        light.getScope().addAll(root3D);
        root3D.getChildren().add(light);
		
		// Add ambient light
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        ambientLight.getScope().addAll(root3D);
        root3D.getChildren().add(ambientLight);
		
		SubScene subScene = new SubScene(pane3D, 824, 724, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.GREY);
        this.pane3D.getChildren().addAll(subScene);
		
        
        
        sliderMap.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				System.out.println("New value : " + newValue);	
				if((double)newValue >= (double)oldValue) {
					camManager.ry.setAngle(camManager.ry.getAngle() - ((double)newValue/10));
				}
				else {
					camManager.ry.setAngle(camManager.ry.getAngle() + ((double)newValue/10));
				}
			}
      	});
        
        
        // Création d'un gestionnaire d'évenement pour le clic ALT + Souris
        subScene.addEventHandler(MouseEvent.ANY, event -> {
        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {
        		PickResult pickResult = event.getPickResult();
        		Point3D spaceCoord = pickResult.getIntersectedPoint();
        		System.out.println(spaceCoord);
        	}
        });
        
		// Création d'un Listener pour le textField via sa fonction textProperty()
		txtName.textProperty().addListener(new ChangeListener<String>(){
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1) {
	           if(t1.equals("")) {
	        	   btnSearch.setDisable(true);
	           }
	           else {
	        	   btnSearch.setDisable(false);
	           }
			}
		});
		
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
			boolean testTxt = false;
			boolean testFirstDate = false;
			boolean testLastDate = false;
			
			if(txtName.getText() == null) {
				txtName.setStyle("fx-border-color: red;");
				testTxt=false;
				
			}
			else {
				testTxt=true;
			}
			
			
			if(btnPeriod.isSelected()) {
				// Si la date n'est pas renseignée alors qu'elle le devrait, on averti l'utilisateur avec du rouge là où il faut entrer une date.
				if(firstDate.getValue() == null) {
					System.out.println("Vous devez entrer une date de début");
					firstDate.setStyle("-fx-border-color: red;");
					testFirstDate = false;
				}
				else {
					firstDate.setStyle("fx-border-color: black;");
					testFirstDate = true;
				}
			
				// Si la date n'est pas renseignée alors qu'elle le devrait, on averti l'utilisateur avec du rouge là où il faut entrer une date.
				if(lastDate.getValue() == null) {
					System.out.println("Vous devez entrer une date de fin");
					lastDate.setStyle("-fx-border-color: red;");
					testLastDate = false;
				}
				else {
					lastDate.setStyle("fx-border-color: black;");
					testLastDate = true;
				}
			}
			
			if(testTxt && testFirstDate && testLastDate) {
				// Faire la requête
				System.out.println("Tout est bon dans le cochon");
				// faire en sorte que les dates soient cohérentes (date1 < date2 obligatoirement)
			}
		});		
	}
	
	public void setDateVisible() {
		paneDate.setVisible(true);
	}
	
	public void setDateNotVisible() {
		paneDate.setVisible(false);
	}
	
	public void setInfosFromRequete(String specieName, String scientificName, String superclass, String order) {
		txtNameInfo.setText(specieName);
		txtScientificNameInfo.setText(scientificName);
		txtSuperclassInfo.setText(superclass);
		txtOrderInfo.setText(order);
	}
}
