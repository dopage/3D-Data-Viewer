package application.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import org.controlsfx.control.textfield.TextFields;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import application.common.Region;
import application.common.Species;
import application.dataAcess.DataProvider;
import application.geohash.GeoHashHelper;
import application.geohash.Location;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.Sphere;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;


public class Controller implements Initializable {
	
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
	
	@FXML
	private VBox vBoxCaptions;
	
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
	
	@FXML
	private Pane pane3DContainer;
	
	private static final float TEXTURE_LAT_OFFSET = -0.2f;
    private static final float TEXTURE_LON_OFFSET = 2.8f;

	private static final double TEXTURE_OFFSET = 1.01;

	ArrayList<Species> speciesRecords = new ArrayList<Species>();
	
	
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
		
		//auto_complete(txtName);
		
		
		// Ajout de Listener sur les Spinners pour pouvoir g�rer la coh�rence de leurs valeurs
		
		firstDate.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
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
		
		Point3D p1 = geoCoordTo3dCoord((float)-27.4710107, (float) 153.0234489); // Melbourne
		Point3D p2 = geoCoordTo3dCoord((float)-39.029020, (float) 146.315101);
		Point3D p3 = geoCoordTo3dCoord((float)	-31.9522400, (float) 115.8614000);
		Point3D p4 = geoCoordTo3dCoord((float)-12.4611300,(float)130.8418500);
		
		addSphere(earth, "a", p1);
		addSphere(earth, "b", p2);
		addSphere(earth, "c", p3);
		addSphere(earth, "d", p4);

		System.out.println(p4);
		
		final PhongMaterial material = new PhongMaterial();
		material.setDiffuseColor(new Color(1, 0, 0.0, 0.1));
        
		AddQuadrilateral(earth, p1, p2, p3, p4, material);
		
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
	    
		PerspectiveCamera camera = new PerspectiveCamera(true);
	    CameraManager camManager = new CameraManager(camera, pane3D, root3D, txtZoom);
	    
		SubScene subScene = new SubScene(root3D, pane3D.getPrefWidth(), pane3D.getPrefHeight(), true, SceneAntialiasing.BALANCED);
		subScene.setCamera(camera);
		subScene.setFill(Color.gray(0.8));
		pane3D.getChildren().addAll(subScene);
		
        // Listener sur le changement de valeur du slider
        sliderMap.valueProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {	
				if((double)newValue >= (double)oldValue) {
					camManager.changeYAngle(-3);
				}
				else {
					camManager.changeYAngle(3);
				}
			}
      	});
        
        pane3DContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
        	pane3D.setPrefWidth(newVal.doubleValue());
        	subScene.setWidth(newVal.doubleValue());
        });
        pane3DContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
        	pane3D.setPrefHeight(newVal.doubleValue());
        	subScene.setHeight(newVal.doubleValue());
        });
        
        // Listener sur le bouton + du zoom
        btnZoomPlus.setOnAction(event -> {
        	camManager.zoomIn(20);
        });
        
        btnZoomMinus.setOnAction(event -> {
        	camManager.zoomOut(20);
        });
        
        // Cr�ation d'un gestionnaire d'�venement pour le clic ALT + Souris
        subScene.addEventHandler(MouseEvent.ANY, event -> {
        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {
        		PickResult pickResult = event.getPickResult();
        		Point3D spaceCoord = pickResult.getIntersectedPoint();
        		Point2D longLatCoord = SpaceCoordToGeoCoord(spaceCoord);
        		if (!Double.isNaN(longLatCoord.getX()) && !Double.isNaN(longLatCoord.getY())) {
        			DataProvider dp = DataProvider.getInstance();
        			final Location loc = new Location("Mouse click", longLatCoord.getX(), longLatCoord.getY());
        			final String locationGeohash = GeoHashHelper.getGeohash(loc);
        			//System.out.println(locationGeohash.substring(0, 3));
        			speciesRecords = dp.getDetailsRecords(locationGeohash.substring(0, 3));
        			System.out.println("Nb species recorded : " + speciesRecords.size());
        			ArrayList<String> speciesNames = new ArrayList<String>();
        			for (Species s : speciesRecords)
        				speciesNames.add(s.getScientificName());
        			Collections.sort(speciesNames);
        			ObservableList<String> itemsListView = FXCollections.observableArrayList(speciesNames);
        			listViewSpecie.setItems(itemsListView);
        		}
        	}
        });
        
        listViewSpecie.setOnMouseClicked( event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)){
				if(event.getClickCount() == 2) {
	                System.out.println("double click (doit exécuter la fonction pour récup les signalements)");
	            }
				else {
					String selectedSpecies = (String) listViewSpecie.getSelectionModel().getSelectedItem();
					for (Species s : speciesRecords) {
						if (s.getScientificName().equals(selectedSpecies)) {
							setInfosFromRequete(s.getSpeciesName(), s.getScientificName(), s.getSuperClass(), s.getOrder());
						}
					}
				}
	        }
		});
        
		// Cr�ation d'un Listener pour le textField via sa fonction textProperty()
		txtName.textProperty().addListener(new ChangeListener<String>(){
			DataProvider dp = DataProvider.getInstance();
			@Override
			public void changed(ObservableValue<? extends String> ov, String t, String t1) {
	           if(t1.equals("")) {
	        	   btnSearch.setDisable(true);
	           }
	           else {
	        	   btnSearch.setDisable(false);	        	   
	           }
	           ArrayList<String> arr =  dp.getScientificNamesBeginWith(txtName.getText());
	           TextFields.bindAutoCompletion(txtName, arr);
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

		drawCaption(0, 100);
	}
	
	public void setDateVisible() {
		paneDate.setVisible(true);
	}
	
	public void setDateNotVisible() {
		paneDate.setVisible(false);
	}
	
	public void setInfosFromRequete(String specieName, String scientificName, String superclass, String order) {
		if (specieName != null)
			txtNameInfo.setText(specieName);
		else
			txtNameInfo.setText("Inconnu");
		if (scientificName != null)
			txtScientificNameInfo.setText(scientificName);
		else
			txtScientificNameInfo.setText("Inconnu");
		if (superclass != null)
			txtSuperclassInfo.setText(superclass);
		else
			txtSuperclassInfo.setText("Inconnu");
		if (order != null)
			txtOrderInfo.setText(order);
		else
			txtOrderInfo.setText("Inconnu");
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
	
	// Fonction pour ajouter une zone
	private void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bottomLeft,
    		Point3D topLeft, PhongMaterial material) {
		
		float coef = (float)1.02;
		
    	topRight = new Point3D(topRight.getX()*coef, topRight.getY()*coef , topRight.getZ()*coef);
    	bottomRight = new Point3D(bottomRight.getX()*coef, bottomRight.getY()*coef , bottomRight.getZ()*coef);
    	topLeft = new Point3D(topLeft.getX()*coef, topLeft.getY()*coef , topLeft.getZ()*coef);
    	bottomLeft = new Point3D(bottomLeft.getX()*coef, bottomLeft.getY()*coef , bottomLeft.getZ()*coef);
    	
    	final TriangleMesh triangleMesh = new TriangleMesh();
    	
    	final float[] points = {
    			(float) topRight.getX(), (float) topRight.getY(), (float) topRight.getZ(),
    			(float) topLeft.getX(), (float) topLeft.getY(), (float) topLeft.getZ(),
    			(float) bottomLeft.getX(), (float) bottomLeft.getY(), (float) bottomLeft.getZ(),
    			(float) bottomRight.getX(), (float) bottomRight.getY(), (float) bottomRight.getZ()	
    	};
    	
    	final float[] texCoords = {
    			1, 1,
    			1, 0,
    			0, 1,
    			0, 0		
    	};
    	
    	final int[] faces = {
    		0, 1, 1, 0, 2, 2,
    		0, 1, 2, 2, 3, 3
    	};
    	
    	triangleMesh.getPoints().setAll(points);
    	triangleMesh.getTexCoords().setAll(texCoords);
    	triangleMesh.getFaces().setAll(faces);
    	
    	final MeshView meshView = new MeshView(triangleMesh);
    	meshView.setMaterial(material);
    	parent.getChildren().addAll(meshView);
    	
    }
	
	public static Affine lookAt(Point3D from, Point3D to, Point3D ydir) {
	    Point3D zVec = to.subtract(from).normalize();
	    Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
	    Point3D yVec = zVec.crossProduct(xVec).normalize();
	    return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
	                      xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
	                      xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
	}
	
	public void addSphere(Group parent, String name, Point3D coord3D) {
    	Group townGroup = new Group();
    	townGroup.setId(name);
    	Sphere sphere = new Sphere(0.01);
    	final PhongMaterial greenMaterial = new PhongMaterial();
        greenMaterial.setDiffuseColor(Color.GREEN);
        greenMaterial.setSpecularColor(Color.GREEN);
    	sphere.setMaterial(greenMaterial);
    	townGroup.getChildren().add(sphere);
    	townGroup.setTranslateX(coord3D.getX());
    	townGroup.setTranslateY(coord3D.getY());
    	townGroup.setTranslateZ(coord3D.getZ());
    	parent.getChildren().addAll(townGroup);
    }
	
	// Fonction qui trace une zone sur la map-monde grâce a des coordonnées (récupérées grâce à une requete de Rayane)
	public void afficheRegionMap(Region r) {
		
	}
	
	public void drawCaption(int min, int max) {
		int nbCaptions = 8;
		vBoxCaptions.getChildren().clear();
		for (int i = 0; i < nbCaptions; i++) {
			Pane p = new Pane();
			p.setPrefWidth(30);
			p.setPrefHeight(30);
			int r = 0 + 255 * i/8;
			int g = 255 - 255 * i/8;
			int b = 0;
			String hex = String.format("%02X%02X%02X", r, g, b);
			p.setStyle("-fx-background-color: #" + hex);
			int rangeMin = min + (max-min)/nbCaptions * i;
			Label lbl = new Label();
			if (i == nbCaptions - 1) {
				lbl.setText("[" + rangeMin + ":" + "+" + "]");
			}
			else {	
				int rangeMax = min + (max-min)/nbCaptions * (i+1);
				lbl.setText("[" + rangeMin + ":" + rangeMax + "]");
			}
			HBox hBox = new HBox();
			hBox.setSpacing(10);
			hBox.setAlignment(Pos.CENTER_LEFT);
			hBox.getChildren().addAll(p, lbl);
			vBoxCaptions.getChildren().add(hBox);
		}
	}
	
	// faire un chargement pour avertir l'utilisateur qu'il doit attendre pour avoir ses résultats.

}
