package application.gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.ResourceBundle;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import application.common.Record;
import application.common.Region;
import application.common.Species;
import application.dataAcess.DataProvider;
import application.exceptions.UnknownSpeciesException;
import application.geohash.GeoHashHelper;
import application.geohash.Location;
import application.util.CoordConverter;
import impl.org.controlsfx.autocompletion.AutoCompletionTextFieldBinding;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.application.Platform;

public class Controller implements Initializable {
	
	// Tous les composants du PaneFind
	@FXML
	private Button btnSearch;
	
	@FXML
	private Label lblUnknownSpecies;
	
	@FXML
	private ToggleButton btnPeriod;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Spinner<Integer> txtGeohash;

	// Tous les composants du PaneDate (qui lui m�me est dans PaneSpecies)
	@FXML
	private Pane paneDate;
	
	@FXML
	private Spinner<Integer> firstDate;
	
	@FXML
	private Spinner<Integer> lastDate;
	
	// Tous les composants du PaneSpecies
	@FXML
	private Pane paneSpecies;
	
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
	private ListView<String> listViewSpecies;
	
	@FXML
	private TableView<Record> tableRecords;
	
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

	ArrayList<Species> speciesRecords = new ArrayList<Species>();
	SuggestionProvider<String> suggestionProvider;
	Group gCourant = new Group();
	DataProvider dp = DataProvider.getInstance();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// Initialisation de la visibilit� des composants
		paneDate.setVisible(false);
		lblUnknownSpecies.setVisible(false);
		
		// Les boutons Play, Pause et Stop sont disable tant qu'aucune recherche bornée par des dates ne sont faites.-
		btnPlay.setDisable(true);
		btnPause.setDisable(true);
		btnStop.setDisable(true);
		
		// Spinner Value Factory pour les spinners de Date
		SpinnerValueFactory<Integer> svf = new SpinnerValueFactory.IntegerSpinnerValueFactory(1920, 2015, 1980, 5);
		SpinnerValueFactory<Integer> svf2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1925, 2020, 2020, 5);
		
		firstDate.setValueFactory(svf);
		lastDate.setValueFactory(svf2);

		SpinnerValueFactory<Integer> svf3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 5, 3, 1);
		txtGeohash.setValueFactory(svf3);
		
		// Ajout de Listener sur les Spinners pour pouvoir g�rer la coh�rence de leurs valeurs
		
		firstDate.valueProperty().addListener((obs, oldVal, newVal) -> {
			if(lastDate.getValue() <= newVal)
				lastDate.increment();
        });
		
		lastDate.valueProperty().addListener((obs, oldVal, newVal) -> {
			if(firstDate.getValue() >= newVal)
				firstDate.decrement();
        });
		
		txtGeohash.valueProperty().addListener((obs, oldVal, newVal) -> {
			dp.setGeohashPrecision(newVal);
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
		earth.getChildren().add(gCourant);
		
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
        sliderMap.valueProperty().addListener((obs, oldVal, newVal) -> {	
        	if(newVal.doubleValue() >= oldVal.doubleValue())
				camManager.changeYAngle(-3);
        	else
				camManager.changeYAngle(3);
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
        btnZoomPlus.setOnAction(event -> { camManager.zoomIn(20); });
        btnZoomMinus.setOnAction(event -> { camManager.zoomOut(20); });
        
        // Cr�ation d'un gestionnaire d'�venement pour le clic ALT + Souris
        subScene.addEventHandler(MouseEvent.ANY, event -> {
        	if(event.getEventType() == MouseEvent.MOUSE_PRESSED && event.isAltDown()) {
        		PickResult pickResult = event.getPickResult();
        		Point3D spaceCoord = pickResult.getIntersectedPoint();
        		Point2D longLatCoord = CoordConverter.SpaceCoordToGeoCoord(spaceCoord);
        		if (!Double.isNaN(longLatCoord.getX()) && !Double.isNaN(longLatCoord.getY())) {
        			final Location loc = new Location("Mouse click", longLatCoord.getX(), longLatCoord.getY());
        			final String locationGeohash = GeoHashHelper.getGeohash(loc);
        			new Thread(new Runnable() {
        			    public void run() {
        			    	speciesRecords = dp.getDetailsRecords(locationGeohash.substring(0, txtGeohash.getValue()));
                			System.out.println("Nb species recorded : " + speciesRecords.size());
                			ArrayList<String> speciesNames = new ArrayList<String>();
                			for (Species s : speciesRecords)
                				speciesNames.add(s.getScientificName());
                			Collections.sort(speciesNames);
                			
                			Runnable command = new Runnable() {
                		        @Override
                		        public void run() {
                        			ObservableList<String> itemsListView = FXCollections.observableArrayList(speciesNames);
                        			listViewSpecies.setItems(itemsListView);
                		        }
                		    };
                		    //Permet d'excuter l'actualisation de l'UI dans le thread principal
                		    Platform.runLater(command);
        			    }
        			}).start();
        		}
        	}
        });
        
        // Initialisation la tableView
		// On crée nos colonnes
	    TableColumn<Record, String> eventDateCol = new TableColumn<Record, String>("EventDate");
	    TableColumn<Record, String> recordedByCol = new TableColumn<Record, String>("RecordedBy");
	    TableColumn<Record, Integer> shoreDistanceCol = new TableColumn<Record, Integer>("ShoreDistance");
	    TableColumn<Record, Integer> bathymetryCol = new TableColumn<Record, Integer>("Bathymetry");
	    
	    // On les ajoute à notre tableView
	    tableRecords.getColumns().add(eventDateCol);
	    tableRecords.getColumns().add(recordedByCol);
	    tableRecords.getColumns().add(shoreDistanceCol);
	    tableRecords.getColumns().add(bathymetryCol);
	    
	    // On définie comment remplir les données de chaque cellule
	    eventDateCol.setCellValueFactory(new PropertyValueFactory<>("eventDate"));
	    recordedByCol.setCellValueFactory(new PropertyValueFactory<>("recorderBy"));
	    shoreDistanceCol.setCellValueFactory(new PropertyValueFactory<>("shoreDistance"));
	    bathymetryCol.setCellValueFactory(new PropertyValueFactory<>("bathymetry"));
	    
        listViewSpecies.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        	for (Species s : speciesRecords) {
				if (s.getScientificName().equals(newVal)) {
					setInfosFromRequete(s.getSpeciesName(), s.getScientificName(), s.getSuperClass(), s.getOrder());
				    ObservableList<Record> itemsListView = FXCollections.observableArrayList(s.getRecords());
				    tableRecords.setItems(itemsListView);
				}
			}
        });
        
        listViewSpecies.setOnMouseClicked( event -> {
			if(event.getButton().equals(MouseButton.PRIMARY)){
				if(event.getClickCount() == 2) {
					String selectedSpeciesName = listViewSpecies.getSelectionModel().getSelectedItem();
					for (Species s : speciesRecords) {
						if (s.getScientificName().equals(selectedSpeciesName)) {
							txtName.setText(s.getScientificName());
							afficheRegionMap(s.getScientificName());
						}
					}
	            }
	        }
		});
        
        txtName.setOnKeyPressed( event -> {
        	if(event.getCode() == KeyCode.ENTER)
        		btnSearch.fire();
        	else {
        		lblUnknownSpecies.setVisible(false);
        		txtName.setStyle("-fx-border-color: rgb(200,200,200); -fx-border-width: 2px;");
        	}
        });
        
		// Cr�ation d'un Listener pour le textField via sa fonction textProperty()
		txtName.textProperty().addListener((ov, t, t1) -> {
			if(t1.equals(""))
	       	   btnSearch.setDisable(true);
			else
				btnSearch.setDisable(false);
			
			new Thread(new Runnable() {
			    public void run() {
			    	ArrayList<String> arr =  dp.getScientificNamesBeginWith(txtName.getText());
			    	Runnable command = new Runnable() {
        		        @Override
        		        public void run() {
        		        	if (suggestionProvider == null) {
        		        		suggestionProvider = SuggestionProvider.create(arr);
        		        		new AutoCompletionTextFieldBinding<>(txtName, suggestionProvider);		
        		        	}
        		        	else {
            		        	suggestionProvider.clearSuggestions();
            		        	suggestionProvider.addPossibleSuggestions(arr);
        		        	}
        		        }
        		    };
        		    //Permet d'excuter l'actualisation de l'UI dans le thread principal
        		    Platform.runLater(command);
			    }
			}).start();
			
		});
		
		// Cr�ation d'un EventListener pour l'interaction avec btnPeriod
		btnPeriod.setOnAction(event -> {
    		if(btnPeriod.isSelected())
    			setDateVisible();
    		else
    			setDateNotVisible();
        });
		
		// Cr�ation d'un EventListener pour l'int�raction avec le bouton Rechercher
		btnSearch.setOnAction(event -> {
			//On vérifie si l'utilsateur a selectionné un interval de temps
			if(btnPeriod.isSelected()) {
				Date d1 = creerDate(firstDate.getValue(), 1, 1);
				Date d2 = creerDate(lastDate.getValue(), 1, 1);
				
				afficheRegionMapByDate(txtName.getText(), d1, d2);
				
				//System.out.println(getNbIntervalsBetween(d1,d2,5));
				
				btnPlay.setDisable(false);
				btnStop.setDisable(false);
				btnPause.setDisable(false);
			}
			else {
				afficheRegionMap(txtName.getText());
			}
		});
		
		btnPlay.setOnAction(event ->{
			animation(getNbIntervalsBetween(creerDate(firstDate.getValue(), 1, 1), creerDate(lastDate.getValue(), 1, 1), 5), creerDate(firstDate.getValue(), 1, 1));
		});
	}
	
	public void setDateVisible() {
		paneDate.setVisible(true);
	}
	
	public void setDateNotVisible() {
		paneDate.setVisible(false);
	}
	
	/**
	 * Fonction qui set les infos d'une espèce sélectionnée dans l'espace associé
	 * @param specieName la nomde l'espèce
	 * @param scientificName le nom scientifique de l'espèce
	 * @param superclass la superclass de l'espèce
	 * @param order l'order de l'espèce
	 */
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
	
	/**
	 * Fonction qui dessine un quadrilatère
	 * @param parent le group dans lequel le quadrilatère sera déssiné
	 * @param topRight le point en haut a droite du quadrilatère
	 * @param bottomRight le point en bas à droite du quadrilatère
	 * @param bottomLeft le point en bas à gauche du quadrilatère
	 * @param topLeft le point en haut à gauche du quadrilatère
	 * @param material le matériel du quadrilatère (sa couleur)
	 */
	private void AddQuadrilateral(Group parent, Point3D topRight, Point3D bottomRight, Point3D bottomLeft, Point3D topLeft, PhongMaterial material) {
		float coef = 1.01f;
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
	
	/**
	 * Fonction qui dessine dynamiquement la légende dans le pane Associé
	 * @param min le minimum d'occurence de l'espèce étudiée
	 * @param max le maximum d'occurence de l'espèce étudiée
	 */
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
	
	/**
	 * Fonction qui retourne la couleur de la zone affichée sur la map. Cette couleur est liée a une couleur de la légende
	 * @param nbCaptions le nombre de légende que l'on a en tout
	 * @param min le minimum d'occurence de l'espèce étudiée.
	 * @param max le maximum d'occurence de l'espèce étudiée.
	 * @param val la valeur 
	 * @return la couleur de la région associée a son nombre d'occurence.
	 */
	public Color getColorforQuadri(int nbCaptions, int min, int max, int val) {
		for (int i = 0; i < nbCaptions; i++) {
			float r = 0 + 1 * (float)(i)/8;
			float g = 1 - 1 * (float)(i)/8;
			float b = 0;
			int rangeMin = min + (max-min)/nbCaptions * i;
			
			if (i == nbCaptions - 1) {
				return new Color(r,g,b, 0.005);
			}
			else {	
				int rangeMax = min + (max-min)/nbCaptions * (i+1);
				if(val >= rangeMin && val < rangeMax) {
					return new Color(r,g,b, 0.005);
				}
			}
		}
		return Color.BLUE;
	}
	
	public void afficheRegionMap(Species s) {
		gCourant.getChildren().clear();
		for(Region r : s.getNbReportsByRegion()) {
			final PhongMaterial pm = new PhongMaterial();
			
			pm.setDiffuseColor(getColorforQuadri(8, s.getMinOccurrence(), s.getMaxOccurrence(), r.getNbReports()));
			
			Point3D p1 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(0).getY(), (float)r.getPoints().get(0).getX());
			Point3D p2 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(1).getY(), (float)r.getPoints().get(1).getX());
			Point3D p3 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(2).getY(), (float)r.getPoints().get(2).getX());
			Point3D p4 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(3).getY(), (float)r.getPoints().get(3).getX());
			
			AddQuadrilateral(gCourant, p4, p3, p2, p1, pm);
		}
	}
	
	
	public void afficheRegionMap(String nameSpecie) {
		try {
			gCourant.getChildren().clear();
			Species s = dp.getNbReportsByRegion(nameSpecie);
					
			// Affichage de la légende associée à l'espèce. Cette légende est unique pour chaque espèce, calculée avec le min et la max des occurences de cette dernière.
			drawCaption(s.getMinOccurrence(), s.getMaxOccurrence());
					
			// Affichage des zones sur la mapMonde 
			for(Region r : s.getNbReportsByRegion()){
				// Transformation des Point2D et point3D
				final PhongMaterial pm = new PhongMaterial();
						
				pm.setDiffuseColor(getColorforQuadri(8, s.getMinOccurrence(), s.getMaxOccurrence(), r.getNbReports()));
				
				Point3D p1 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(0).getY(), (float)r.getPoints().get(0).getX());
				Point3D p2 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(1).getY(), (float)r.getPoints().get(1).getX());
				Point3D p3 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(2).getY(), (float)r.getPoints().get(2).getX());
				Point3D p4 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(3).getY(), (float)r.getPoints().get(3).getX());
				
				AddQuadrilateral(gCourant, p4, p3, p2, p1, pm);
			}
		} catch (UnknownSpeciesException e) {
			//e.printStackTrace();
			System.err.println("Erreur dans la requete afficheRegionMap()");
    		lblUnknownSpecies.setVisible(true);
			txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
	}
	
	public void afficheRegionMapByDate(String nameSpecie, Date from, Date to) {
		try {
			gCourant.getChildren().clear();
			Species s = dp.getNbReportsByRegion(nameSpecie, from, to);
			
			// Affichage de la légende associée à l'espèce. Cette légende est unique pour chaque espèce, calculée avec le min et la max des occurences de cette dernière.
			drawCaption(s.getMinOccurrence(), s.getMaxOccurrence());
			
			// Affichage des zones sur la mapMonde 
			for(Region r : s.getNbReportsByRegion()) {
				// Transformation des Point2D et point3D
				final PhongMaterial pm = new PhongMaterial();
				pm.setDiffuseColor(getColorforQuadri(8, s.getMinOccurrence(), s.getMaxOccurrence(), r.getNbReports()));
				
				Point3D p1 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(0).getY(), (float)r.getPoints().get(0).getX());
				Point3D p2 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(1).getY(), (float)r.getPoints().get(1).getX());
				Point3D p3 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(2).getY(), (float)r.getPoints().get(2).getX());
				Point3D p4 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(3).getY(), (float)r.getPoints().get(3).getX());
				
				AddQuadrilateral(gCourant, p4, p3, p2, p1, pm);
			}
		} catch (UnknownSpeciesException e) {
			//e.printStackTrace();
			System.err.println("Erreur dans la requete afficheRegionMapByDate()");
    		lblUnknownSpecies.setVisible(true);
			txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
	}
	

	/**
	 * Fonction pour créer une date de manière propre
	 * @param year l'année de la date
	 * @param month le mois de la date
	 * @param day le jour de la date
//	 * @return un objet de type DATE avec les valeurs en paramètres
	 */
	public Date creerDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return date;
    }
	
	// faire un chargement pour avertir l'utilisateur qu'il doit attendre pour avoir ses résultats.

	public void animation(int nbIntervals, Date d1) {
		
		DataProvider dp = DataProvider.getInstance();
		ArrayList<Species> tabSpecies = new ArrayList<Species>();
		
		try {
			tabSpecies = dp.getNbReportsByRegionByTimeInterval(txtName.getText(), null, d1, 5, nbIntervals);
		} catch (UnknownSpeciesException e) {
			e.printStackTrace();
		}
		
		for(Species s : tabSpecies) {
			// Dans tabSpecies on a une instance d'espèce qui correspond a un intervalle de temps (5 ans).
			// Il suffit maintenant que l'on affiche les données de ces espèces toutes les 5 secondes et ça fera une animation :
//			try {
//				System.out.println("je sleep");
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println("j'affiche les map");
//			afficheRegionMap(s);
			System.out.println("Espece de l'intervalle ----> " + s );
			
			
		}
	}
	
	/**
	 * Fonction qui retourne le nombre d'intervalle d'une durée 'pas' entre deux dates d1 et d2 
	 * @param d1 la date de début
	 * @param d2 la date de fin
	 * @param pas : la valeur du pas de l'intervalle. Dans notre cas : 5 car on veut qu'un intervalle soit de 5 ans.
	 * @return
	 */
	public int getNbIntervalsBetween(Date d1, Date d2, int pas) {
		return (getYear(d2) - getYear(d1)) / pas;
	}
	
	/**
	 * Retourne l'année d'une Date de manière propre
	 * @param date la date en question
	 * @return l'année de la date en question
	 */
	public int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }
}
