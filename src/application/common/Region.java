package application.common;

import java.util.ArrayList;

import javafx.geometry.Point2D;

public class Region {
	private ArrayList<Point2D> points;
	private int nbReports;
	
	public Region(ArrayList<Point2D> points, int nbReports) {
		super();
		this.points = points;
		this.nbReports = nbReports;
	}
	
	public ArrayList<Point2D> getPoints() {
		return points;
	}
	
	public int getNbReports() {
		return nbReports;
	}
	
	
}
