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
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
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
	
	@FXML
	private Label lblCurrentInterval;
	
	// Tous les composants du Pane3D
	@FXML
	private Pane pane3D;
	
	@FXML
	private Pane pane3DContainer;

	private ArrayList<Species> speciesRecords = new ArrayList<Species>();
	private SuggestionProvider<String> suggestionProvider;
	private Group gCourant = new Group();
	private DataProvider dp = DataProvider.getInstance();
	private int lastSelectedZoneRequestID = 0;
	private Species sCurrent;
	private ArrayList<Species> speciesForAnimation;
	private boolean anAnimationIsRunning = false;
	private boolean animationIsPlaying = false;
	
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
        	double delta = newVal.doubleValue() - oldVal.doubleValue();
        	camManager.changeYAngle(360 * delta);
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
        			setSpeciesInfos("", "", "", "");
        			tableRecords.setItems(null);
        			listViewSpecies.setItems(FXCollections.observableArrayList("Chargement..."));
        			final Location loc = new Location("Mouse click", longLatCoord.getX(), longLatCoord.getY());
        			final String locationGeohash = GeoHashHelper.getGeohash(loc);
        			new Thread(new Runnable() {
        			    public void run() {
        			    	lastSelectedZoneRequestID++;
        			    	int requestID = lastSelectedZoneRequestID;
        			    	speciesRecords = dp.getDetailsRecords(locationGeohash);
                			ArrayList<String> speciesNames = new ArrayList<String>();
                			for (Species s : speciesRecords)
                				speciesNames.add(s.getScientificName());
                			Collections.sort(speciesNames);
                			
                			Runnable command = new Runnable() {
                		        @Override
                		        public void run() {
                		        	if (lastSelectedZoneRequestID == requestID) {
                		        		ObservableList<String> itemsListView = FXCollections.observableArrayList(speciesNames);
                		        		listViewSpecies.setItems(itemsListView);
                		        	}
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
	    
	    // EventListener sur la liste des Espèces (après un ALT + CLIC)
        listViewSpecies.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        	for (Species s : speciesRecords) {
				if (s.getScientificName().equals(newVal)) {
					setSpeciesInfos(s.getSpeciesName(), s.getScientificName(), s.getSuperClass(), s.getOrder());
				    ObservableList<Record> itemsListView = FXCollections.observableArrayList(s.getRecords());
				    tableRecords.setItems(itemsListView);
				}
			}
        });
        
        // EventListener pour que lorsque l'on double clique sur un element de la liste, cela effectue la recherche
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
					if(btnHisto.isSelected()) {
						modeHisto();
					}
	            }
	        }
		});
        
        // EventListener pour que lorsque l'on appuie sur ENTREE cela effectue la recherche
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
				Date d1 = createDate(firstDate.getValue(), 0, 1);
				Date d2 = createDate(lastDate.getValue(), 0, 1);
				
				afficheRegionMapByDate(txtName.getText(), d1, d2);
				
				btnPlay.setDisable(false);
				btnPause.setDisable(true);
				btnStop.setDisable(true);
			}
			else {
				afficheRegionMap(txtName.getText());
				btnPlay.setDisable(true);
				btnPause.setDisable(true);
				btnStop.setDisable(true);
			}
			anAnimationIsRunning = false;
			animationIsPlaying = false;
			if(btnHisto.isSelected()) {
				modeHisto();
			}
		});
		
		btnPlay.setOnAction(event -> {
			if (!animationIsPlaying) {
				if (anAnimationIsRunning) {
					animationIsPlaying = true;
				}
				else {
					int nbIntervals = (lastDate.getValue() - firstDate.getValue()) / 5;
					animation(nbIntervals, createDate(firstDate.getValue(), 0, 1));
					anAnimationIsRunning = true;
					animationIsPlaying = true;
				}
				btnPlay.setDisable(true);
				btnStop.setDisable(false);
				btnPause.setDisable(false);
			}
		});
		
		btnPause.setOnAction(event -> {
			if (animationIsPlaying) {
				btnPlay.setDisable(false);
				btnStop.setDisable(false);
				btnPause.setDisable(true);
				animationIsPlaying = false;
			}
		});
		
		btnStop.setOnAction(event -> {
			btnPlay.setDisable(false);
			btnStop.setDisable(true);
			btnPause.setDisable(true);
			gCourant.getChildren().clear();
			lblCurrentInterval.setText("");
			animationIsPlaying = false;
			anAnimationIsRunning = false;
		});
		
		// EventListener du bouton Histogramme
		btnHisto.setOnAction(event -> {
			if(btnHisto.isSelected()) {
				modeHisto();
			}
			else {
				afficheRegionMap(sCurrent);
			}
		});
		
		// On charge le fichier json avec les données de bases
		showRegionsMapFromFile("data/Delphinidae.json", "Delphinidae");
		txtName.setText("Delphinidae");
	}
	
	/**
	 * Fonction qui fait en sorte que les dates soient visible par l'utilisateur
	 */
	public void setDateVisible() {
		paneDate.setVisible(true);
	}
	
	/**
	 * Fonction qui fait en sorte que les dates ne soient pas visible par l'utilisateur
	 */
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
	public void setSpeciesInfos(String specieName, String scientificName, String superclass, String order) {
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
		float coef = 1.0025f;
		
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
	
	/**
	 * Fonction pour orienter les barres de l'histogramme vers le centre de la terre.
	 * @param from
	 * @param to
	 * @param ydir
	 * @return
	 */
	public static Affine lookAt(Point3D from, Point3D to, Point3D ydir) {
	    Point3D zVec = to.subtract(from).normalize();
	    Point3D xVec = ydir.normalize().crossProduct(zVec).normalize();
	    Point3D yVec = zVec.crossProduct(xVec).normalize();
	    return new Affine(xVec.getX(), yVec.getX(), zVec.getX(), from.getX(),
	                      xVec.getY(), yVec.getY(), zVec.getY(), from.getY(),
	                      xVec.getZ(), yVec.getZ(), zVec.getZ(), from.getZ());
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
			int r = 255 - 255 * i/8;
			int g = 0;
			int b = 255 * i/8;
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
			float r = 1 - (float)(i)/8;
			float g = 0;
			float b = 1 * (float)i/8;
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
		return Color.WHITE;
	}
	
	/**
	 * Fonction qui retourne la couleur de la box affichée sur la map. Cette couleur est liée a une couleur de la légende
	 * @param nbCaptions le nombre de légende que l'on a en tout
	 * @param min le minimum d'occurence de l'espèce étudiée.
	 * @param max le maximum d'occurence de l'espèce étudiée.
	 * @param val la valeur 
	 * @return la couleur de la région associée a son nombre d'occurence.
	 */
	public Color getColorforBox(int nbCaptions, int min, int max, int val) {
		for (int i = 0; i < nbCaptions; i++) {
			float r = 1 - (float)(i)/8;
			float g = 0;
			float b = 1 * (float)i/8;
			int rangeMin = min + (max-min)/nbCaptions * i;
			
			if (i == nbCaptions - 1) {
				return new Color(r,g,b, 0.5);
			}
			else {	
				int rangeMax = min + (max-min)/nbCaptions * (i+1);
				if(val >= rangeMin && val < rangeMax) {
					return new Color(r,g,b, 0.5);
				}
			}
		}
		return Color.WHITE;
	}
	
	/**
	 * Fonction pour afficher les régions sur la map via l'instance d'une espèce
	 * @param s l'instance d'espèce
	 */
	public void afficheRegionMap(Species s) {
		sCurrent = s;
		if (btnHisto.isSelected()) {
			modeHisto();
		}
		else {
			gCourant.getChildren().clear();
			int averageTop10Perc = getAverageTop10Pec(s.getNbReportsByRegion());
			drawCaption(s.getMinOccurrence(), averageTop10Perc);
			
			for(Region r : s.getNbReportsByRegion()) {
				final PhongMaterial pm = new PhongMaterial();
				pm.setDiffuseColor(getColorforQuadri(8, s.getMinOccurrence(), averageTop10Perc, r.getNbReports()));
				
				Point3D p1 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(0).getY(), (float)r.getPoints().get(0).getX());
				Point3D p2 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(1).getY(), (float)r.getPoints().get(1).getX());
				Point3D p3 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(2).getY(), (float)r.getPoints().get(2).getX());
				Point3D p4 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(3).getY(), (float)r.getPoints().get(3).getX());
				
				AddQuadrilateral(gCourant, p4, p3, p2, p1, pm);
			}
		}
	}
	
	/**
	 * Fonction pour afficher les régions sur le map via un fichier json
	 * @param path le chemin du fichier json
	 * @param scientificName le nom de l'espèce
	 */
	public void showRegionsMapFromFile(String path, String scientificName) {
		try {
			gCourant.getChildren().clear();
			sCurrent = dp.getNbReportsByRegionFromFile(path, scientificName);
			
			// Affichage de la légende associée à l'espèce. Cette légende est unique pour chaque espèce, calculée avec le min et la max des occurences de cette dernière.
			int averageTop10Perc = getAverageTop10Pec(sCurrent.getNbReportsByRegion());
			drawCaption(sCurrent.getMinOccurrence(), averageTop10Perc);
			
			// Affichage des zones sur la mapMonde 
			for(Region r : sCurrent.getNbReportsByRegion()){
				// Transformation des Point2D et point3D
				final PhongMaterial pm = new PhongMaterial();
				pm.setDiffuseColor(getColorforQuadri(8, sCurrent.getMinOccurrence(), averageTop10Perc, r.getNbReports()));
				
				Point3D p1 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(0).getY(), (float)r.getPoints().get(0).getX());
				Point3D p2 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(1).getY(), (float)r.getPoints().get(1).getX());
				Point3D p3 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(2).getY(), (float)r.getPoints().get(2).getX());
				Point3D p4 = CoordConverter.geoCoordTo3dCoord((float)r.getPoints().get(3).getY(), (float)r.getPoints().get(3).getX());
				AddQuadrilateral(gCourant, p4, p3, p2, p1, pm);
			}
		} catch (UnknownSpeciesException e) {
			//e.printStackTrace();
			System.err.println("Erreur dans la requete showRegionsMapFromFile()");
    		lblUnknownSpecies.setVisible(true);
			txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
		}
	}
	
	/**
	 * Fonction pour afficher les régions sur la map
	 * @param nameSpecies le nom de l'espèce
	 */
	public void afficheRegionMap(String nameSpecies) {
		gCourant.getChildren().clear();
		new Thread(new Runnable() {
		    public void run() {
		    	try {
		    		Species s = dp.getNbReportsByRegion(nameSpecies);
					Runnable command = new Runnable() {
	    		        @Override
	    		        public void run() {
	    		        	afficheRegionMap(s);
	    		        }
	    		    };
	    		    //Permet d'excuter l'actualisation de l'UI dans le thread principal
	    		    Platform.runLater(command);
		    	} catch (UnknownSpeciesException e) {
		    		lblUnknownSpecies.setVisible(true);
					txtName.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
				}
		    }
		}).start();
	}
	
	/**
	 * Fonction pour afficher les régions sur la map avec un interval de temps
	 * @param nameSpecies le nom de l'espèce
	 * @param from : date de début
	 * @param to : date de fin
	 */
	public void afficheRegionMapByDate(String nameSpecies, Date from, Date to) {
		try {
			gCourant.getChildren().clear();
			sCurrent = dp.getNbReportsByRegion(nameSpecies, from, to);
			
			// Affichage de la légende associée à l'espèce. Cette légende est unique pour chaque espèce, calculée avec le min et la max des occurences de cette dernière.
			int averageTop10Perc = getAverageTop10Pec(sCurrent.getNbReportsByRegion());
			drawCaption(sCurrent.getMinOccurrence(), averageTop10Perc);
			
			// Affichage des zones sur la mapMonde 
			for(Region r : sCurrent.getNbReportsByRegion()) {
				// Transformation des Point2D et point3D
				final PhongMaterial pm = new PhongMaterial();
				pm.setDiffuseColor(getColorforQuadri(8, sCurrent.getMinOccurrence(), averageTop10Perc, r.getNbReports()));
				
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
	 Permet de calculer la valeur moyenne du top 10% des signalements les plus élevés
	 * @param arrRegions : la liste des regions
	 * @return : retourne la moyenne du top 10%
	 */
	public int getAverageTop10Pec(ArrayList<Region> arrRegions) {
		int averageTop10Perc = 0;
		int nbElTop10Perc = Math.round(arrRegions.size() * 0.1f);
		arrRegions.sort((o1, o2) -> {
			return Integer.compare(o2.getNbReports(), o1.getNbReports());
		});
		for (int i = 0; i < nbElTop10Perc; i++) {
			averageTop10Perc += arrRegions.get(i).getNbReports();
		}
		if (nbElTop10Perc > 0)
			averageTop10Perc /= nbElTop10Perc;
		return averageTop10Perc;
	}

	/**
	 * Fonction pour créer une date de manière propre
	 * @param year l'année de la date
	 * @param month le mois de la date
	 * @param day le jour de la date
//	 * @return un objet de type DATE avec les valeurs en paramètres
	 */
	public static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = calendar.getTime();
        return date;
    }
	
	// faire un chargement pour avertir l'utilisateur qu'il doit attendre pour avoir ses résultats.
	
	public void animation(int nbIntervals, Date from) {
		DataProvider dp = DataProvider.getInstance();
		speciesForAnimation = null;
		
		new Thread(new Runnable() {
		    public void run() {
		    	try {
		    		// Dans speciesForAnimation on a des instances de la même espèce qui correspondant à des intervalles de temps de 5 ans
					speciesForAnimation = dp.getNbReportsByRegionByTimeInterval(txtName.getText(), from, 5, nbIntervals);
					int i = 0;
					for(Species s : speciesForAnimation) {
						try {
							if (anAnimationIsRunning) {
								while(anAnimationIsRunning && !animationIsPlaying) {
									Thread.sleep(500);
								}
								if (!anAnimationIsRunning)
									break;
								String from = String.valueOf(firstDate.getValue() + 5*i);
								String to = String.valueOf(firstDate.getValue() + 5*(i+1));
								Platform.runLater(() -> {
									afficheRegionMap(s);
									lblCurrentInterval.setText(from + "-" + to);
								});
								Thread.sleep(2000);
							}
							else
								break;
						} catch (InterruptedException e) {
		    				e.printStackTrace();
		    			}
						i++;
		    		}
		    	} catch (UnknownSpeciesException e) {
					e.printStackTrace();
				}
		    }
		}).start();
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
	
	public void modeHisto() {
		gCourant.getChildren().clear();
		
		int averageTop10Perc = getAverageTop10Pec(sCurrent.getNbReportsByRegion());
		drawCaption(sCurrent.getMinOccurrence(), averageTop10Perc);
		
		// Affichage des zones sur la mapMonde 
		for(Region r : sCurrent.getNbReportsByRegion()){
			// Couleur de la box
			final PhongMaterial pm = new PhongMaterial();
			pm.setDiffuseColor(getColorforBox(8, sCurrent.getMinOccurrence(), averageTop10Perc, r.getNbReports()));
			
			// Calcul de la distance p1/p4 et p3/p4
			float xP1 = (float)r.getPoints().get(0).getX();
			float xP4 = (float)r.getPoints().get(3).getX();
			float xP3 = (float)r.getPoints().get(2).getX();
			float xP2 = (float)r.getPoints().get(1).getX();
			
			float yP1 = (float)r.getPoints().get(0).getY();
			float yP4 = (float)r.getPoints().get(3).getY();
			float yP3 = (float)r.getPoints().get(2).getY();
			float yP2 = (float)r.getPoints().get(2).getY();
			
			float distanceP1P4 = Distance(xP1, yP1, xP4, yP4); // Valeur distance P1-P4
			float distanceP4P3 = Distance(xP4, yP4, xP3, yP3); // Valeur distance P4-P3
			
			// Calcul du centre du quadrilatère
			Point2D mP1P4 = new Point2D((xP1+xP4)/2, (yP1+yP4)/2); // Coordonnées du centre du segment P1-P4
			Point2D mP3P2 = new Point2D((xP3+xP2)/2, (yP3+yP2)/2); // Coordonnées du centre du segment P3-P2
			
			Point2D Centre = new Point2D((mP1P4.getX()+mP3P2.getX())/2 , (mP1P4.getY()+mP3P2.getY())/2); // Coordonnées du centre du quadrilatère (P1,P2,P3,P4)
			
			// Normalisation des valeurs de la hauteur
			float size = (float)r.getNbReports()/averageTop10Perc;
			if(size>1) size=1;
			
			// Affichage de la Box associée
			AddBox(gCourant, (float)Centre.getX(), (float)Centre.getY(), size, distanceP1P4/100, distanceP4P3/100, pm);
		}
	}
	
	/**
	 * Fonction qui calcule la distance entre deux points via leurs coordonnées
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return la distance entre les 2 points
	 */
	static public float Distance(double x1, double y1, double x2, double y2) {
        return (float)Math.sqrt(Math.pow((y2 - y1), 2) + Math.pow((x2 - x1),2));
    }
	
	/**
	 * Fonction qui ajoute une box (parallépipède) a un Group
	 * @param parent le group 
	 * @param lon la longitude 
	 * @param lat la latitude
	 * @param size la taille (hauteur) de la box
	 * @param x la longueur x 
	 * @param y la longueur y (en fait c'est la z)
	 * @param m la couleur (=le materiel) de la box
	 */
	public void AddBox(Group parent, float lon, float lat, float size, float x, float y, PhongMaterial m) {
		Group g = new Group();
		Affine a = new Affine();
		
		Box box = new Box(x, y, size);
		
		Point3D from = CoordConverter.geoCoordTo3dCoord(lat, lon);
		Point3D to = Point3D.ZERO;
		Point3D yDir = new Point3D(1, 0, 1);
		
		box.setMaterial(m);
		a.append(lookAt(from, to, yDir));
		
		g.getTransforms().setAll(a);
		g.getChildren().addAll(box);
		
		parent.getChildren().addAll(g);
	}
}
