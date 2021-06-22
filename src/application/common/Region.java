package application.common;

import java.util.ArrayList;

import application.util.Point_2D;

public class Region {
	private ArrayList<Point_2D> points;
	private int nbReports;
	
	public Region(ArrayList<Point_2D> points, int nbReports) {
		super();
		this.points = points;
		this.nbReports = nbReports;
	}
	
	public ArrayList<Point_2D> getPoints() {
		return points;
	}
	
	public int getNbReports() {
		return nbReports;
	}
	
	
}
