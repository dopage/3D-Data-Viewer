package application.gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import application.common.Region;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
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
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MeshView;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;


public class Controller implements Initializable {
	
	// Soyons Organis�
	
	// Tous les composants du PaneFind
	@FXML
	private Pane paneFind;
	
	@FXML
	private Button btnSearch;
	
	@FXML
	private ToggleButton btnPeriod;
	
	@FXML
	private TextField txtName;

	// Tous les composants du PaneDate (qui lui m�me est dans PaneSpecie)
	@FXML
	private Pane paneDate;
	
	@FXML
	private Spinner<Integer> firstDate;
	
	@FXML
	private Spinner<Integer> lastDate;
	
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
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

	private static final double TEXTURE_OFFSET = 1.01;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Initialisation de la visibilit� des composants
		//setDateNotVisible();
		paneDate.setVisible(false);
		btnSearch.setDisable(true);
		
		txtNameInfo.setDisable(true);
		txtScientificNameInfo.setDisable(true);
		txtSuperclassInfo.setDisable(true);
		txtOrderInfo.setDisable(true);
		
		// Spinner Value Factory pour les spinners de Date
		SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1920, 2020, 1920, 5);
		SpinnerValueFactory<Integer> svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1925, 2020, 1925, 5);
		
		firstDate.setValueFactory(svf);
		lastDate.setValueFactory(svf2);
		
		// Ajout de Listener sur les Spinners pour pouvoir g�rer la coh�rence de leurs valeurs
		
		firstDate.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				// TODO Auto-generated method stub
				if(lastDate.getValue() <= arg2) {
					lastDate.increment();
				}
			}
        });
		
		lastDate.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if(lastDate.getValue() >= arg2) {
					firstDate.decrement();
				}
			}
        });
		
		PerspectiveCamera camera = new PerspectiveCamera(true);
		Group root3D = new Group();
		
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
	    
	    CameraManager camManager = new CameraManager(camera, pane3D, root3D);
	    
		SubScene subScene = new SubScene(root3D, 824, 724, true, SceneAntialiasing.BALANCED);
		subScene.setCamera(camera);
		subScene.setFill(Color.GREY);
		pane3D.getChildren().addAll(subScene);
		
        // Listener sur le changement de valeur du slider
        sliderMap.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	
				if((double)newValue >= (double)oldValue) {
					camManager.ry.setAngle(camManager.ry.getAngle() - 3);
				}
				else {
					camManager.ry.setAngle(camManager.ry.getAngle() + 3);
				}
			}
      	});
        
        // Listener sur le bouton + du zoom
        btnZoomPlus.setOnAction(event ->{
        	double z = camera.getTranslateZ();
            double newZ = z + 0.1;
            if (newZ > -1.1) {
            	newZ = -1.1;
            	txtZoom.setText("MAX");
            }
            else {
            	txtZoom.setText("" + newZ);
            }
            camera.setTranslateZ(newZ);
            
        });
        
        btnZoomMinus.setOnAction(event ->{
        	double z = camera.getTranslateZ();
            double newZ = z - 0.1;
            if (newZ < -6.9) {
            	newZ = -6.9;
            	txtZoom.setText("MIN");
            }
            else {
            	txtZoom.setText("" + newZ);
            }
            camera.setTranslateZ(newZ);
        	
        	// z = 4 -> 100%
        	// z = 1.1 -> max
        	// z = -6.9 -> min
        });
        
        // Cr�ation d'un gestionnaire d'�venement pour le clic ALT + Souris
        subScene.addEventHandler(MouseEvent.ANY, event -> {
        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {
        		PickResult pickResult = event.getPickResult();
        		Point3D spaceCoord = pickResult.getIntersectedPoint();
        		Point2D longLatCoord = SpaceCoordToGeoCoord(spaceCoord);
        	}
        });
        
		// Cr�ation d'un Listener pour le textField via sa fonction textProperty()
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
		
		// Cr�ation d'un EventListener pour l'interaction avec btnPeriod
		btnPeriod.setOnAction(event ->  
    	{
    		if(btnPeriod.isSelected()) {
    			setDateVisible();
    		}
    		else {
    			setDateNotVisible();
    		}
        });
		
		// Cr�ation d'un EventListener pour l'int�raction avec le bouton Rechercher
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
				// faire des trucs avec les dates
				testLastDate = true;
			}
			
			if(testTxt && testFirstDate && testLastDate) {
				// Faire la requ�te
				System.out.println("Tout est bon dans le cochon");
				// faire en sorte que les dates soient coh�rentes (date1 < date2 obligatoirement)
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
	
	public static Point3D geoCoordTo3dCoord(float lat, float lon) {
        float lat_cor = lat + TEXTURE_LAT_OFFSET;
        float lon_cor = lon + TEXTURE_LON_OFFSET;
        return new Point3D(
                -java.lang.Math.sin(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)),
                -java.lang.Math.sin(java.lang.Math.toRadians(lat_cor)),
                java.lang.Math.cos(java.lang.Math.toRadians(lon_cor))
                        * java.lang.Math.cos(java.lang.Math.toRadians(lat_cor)));
    }
	
	public static Point2D SpaceCoordToGeoCoord(Point3D p) {
    	
	    float lat = (float) (Math.asin(-p.getY() / TEXTURE_OFFSET) 
	                          * (180 / Math.PI) - TEXTURE_LAT_OFFSET);
	    float lon;
	        
	    if (p.getZ() < 0) {
	    	lon = 180 - (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET 
		        * Math.cos((Math.PI / 180) 
	               * (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI + TEXTURE_LON_OFFSET);
	    } else {
	    	lon = (float) (Math.asin(-p.getX() / (TEXTURE_OFFSET * Math.cos((Math.PI / 180) 
	    		* (lat + TEXTURE_LAT_OFFSET)))) * 180 / Math.PI - TEXTURE_LON_OFFSET);
	    }
	        
	    return new Point2D(lat, lon);    
	}
	
	// Fonction qui trace une zone sur la map-monde grâce a des coordonnées (récupérées grâce à une requete de Rayane)
	public void afficheRegionMap(Region r) {
		
	}
	
	public void dessineLegende() {
		
	}
	
	// faire un chargement pour avertir l'utilisateur qu'il doit attendre pour avoir ses résultats.

}
