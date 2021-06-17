package application.gui;

import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Rotate;

public class CameraManager {

    private static final double CAMERA_MIN_DISTANCE = -1.2;
    private static final double CAMERA_MAX_DISTANCE = -7.0;
    private static final double CAMERA_INITIAL_DISTANCE = -5;
    private static final double CAMERA_INITIAL_X_ANGLE = 0.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 0.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    private static final double CONTROL_MULTIPLIER = 0.1;
    private static final double SHIFT_MULTIPLIER = 10.0;
    private static final double MOUSE_SPEED = 0.025;
    private static final double ZOOM_SPEED = 0.01;
    private static final double ROTATION_SPEED = 1.0;
    private static final double TRACK_SPEED = 0.6;

    private final Group cameraXform = new Group();
    private final Group cameraXform2 = new Group();
    private Rotate rx = new Rotate();
    private Rotate ry = new Rotate();
    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private double currentCameraDistance;
    
    private Camera camera;
    private TextField txtZoom;
//    @FXML 
//	private Slider sliderMap;
    
    
    public CameraManager(Camera cam, Node mainRoot, Group root, TextField txtZoom) {

        camera = cam;
        this.txtZoom = txtZoom;
        txtZoom.setText(String.valueOf(CAMERA_INITIAL_DISTANCE));
        
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(camera);

        rx.setAxis(Rotate.X_AXIS);
        ry.setAxis(Rotate.Y_AXIS);
        cameraXform.getTransforms().addAll(ry, rx);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        rx.setAngle(CAMERA_INITIAL_X_ANGLE);

        currentCameraDistance = CAMERA_INITIAL_DISTANCE;
        
        // Add keyboard and mouse handler
        handleKeyboard(mainRoot, root);
        handleMouse(mainRoot, root);

        updateTxtZoom();
    }
    
    public Camera getCamera() {
    	return camera;
    }
    
    public void setCamera(Camera newC) {
    	this.camera = newC;
    }

    public Rotate getRy() {
    	return ry;
    }
    
    public void setRy(Rotate newR) {
    	this.ry = newR;
    }
    
    public void changeYAngle(double delta) {
    	ry.setAngle(ry.getAngle() + delta);
    }
    
    public void zoomIn(double delta) {
        double newZ = currentCameraDistance + delta * ZOOM_SPEED;
        if (newZ > CAMERA_MIN_DISTANCE) {
        	newZ = CAMERA_MIN_DISTANCE;
            camera.setTranslateZ(newZ);
            currentCameraDistance = newZ;
        }
        else {
            camera.setTranslateZ(newZ);
            currentCameraDistance = newZ;
        }
        updateTxtZoom();
    }
    
    public void zoomOut(double delta) {
    	double newZ = currentCameraDistance - delta * ZOOM_SPEED;
        if (newZ < CAMERA_MAX_DISTANCE) {
        	newZ = CAMERA_MAX_DISTANCE;
            camera.setTranslateZ(newZ);
            currentCameraDistance = newZ;
        }
        else {
            camera.setTranslateZ(newZ);
            currentCameraDistance = newZ;
        }
        updateTxtZoom();
    }
    
    public double getCurrentCameraDistance() {
    	return currentCameraDistance;
    }
    
    private void handleMouse(Node mainRoot, final Node root) {

        mainRoot.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();

                // Set focus on the mainRoot to be able to detect key press
                mainRoot.requestFocus();
            }
        });
        
        mainRoot.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (me.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                if (me.isPrimaryButtonDown()) {
                	double rotation_speed = ROTATION_SPEED; 
                	if (Math.abs(currentCameraDistance) < 3) {
                		if (Math.abs(currentCameraDistance) < 2) {
                    		rotation_speed  = ROTATION_SPEED * Math.abs(currentCameraDistance)/50;
                		}
                		else {
                    		rotation_speed  = ROTATION_SPEED * Math.abs(currentCameraDistance)/10;
                		}
                	}
                    ry.setAngle(ry.getAngle() + mouseDeltaX * modifier * rotation_speed);
                    rx.setAngle(rx.getAngle() - mouseDeltaY * modifier * rotation_speed);
                } else if (me.isSecondaryButtonDown()) {
                	cameraXform2.setTranslateX(cameraXform2.getTranslateX() - mouseDeltaX * MOUSE_SPEED * modifier * TRACK_SPEED);
                    cameraXform2.setTranslateY(cameraXform2.getTranslateY() - mouseDeltaY * MOUSE_SPEED * modifier * TRACK_SPEED);
                }
            }
        });
        
        mainRoot.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                double modifier = 1.0;

                if (event.isControlDown()) {
                    modifier = CONTROL_MULTIPLIER;
                }
                if (event.isShiftDown()) {
                    modifier = SHIFT_MULTIPLIER;
                }
                double newZ = currentCameraDistance + event.getDeltaY() * ZOOM_SPEED * modifier;
                if (newZ > CAMERA_MIN_DISTANCE) newZ = CAMERA_MIN_DISTANCE;
                else if (newZ < CAMERA_MAX_DISTANCE) newZ = CAMERA_MAX_DISTANCE;
                camera.setTranslateZ(newZ);
                currentCameraDistance = newZ;
                updateTxtZoom();
            }
        });
    }
    
    private void updateTxtZoom() {
    	double ecart = CAMERA_MAX_DISTANCE - CAMERA_MIN_DISTANCE;
        double val = (ecart - (currentCameraDistance - CAMERA_MIN_DISTANCE))/ecart * 100;
        txtZoom.setText((int) val + "%");
    }

    private void handleKeyboard(Node mainRoot, final Node root) {
        mainRoot.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
				default:
					break;
//                    case ALT:
//                        cameraXform2.setTranslateX(0.0);
//                        cameraXform2.setTranslateY(0.0);
//
//                        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
//
//                        ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
//                        rx.setAngle(CAMERA_INITIAL_X_ANGLE);
//                        break;
//                    default:

                }
            }
        });
    }
}