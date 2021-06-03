package application.common;

import java.util.ArrayList;

public class Species {

	private String speciesName;
	private String scientificName;
	private String order;
	private String superClass;
	private int maxOccurrence;
	private int minOccurrence;
	
	ArrayList<Region> nbReportsByRegion;
	ArrayList<Record> records;
	
	public Species(String speciesName, String scientificName, int maxOccurrence, int minOccurrence) {
		super();
		this.speciesName = speciesName;
		this.scientificName = scientificName;
		this.order = null;
		this.superClass = null;
		this.maxOccurrence = maxOccurrence;
		this.minOccurrence = minOccurrence;
		nbReportsByRegion = new ArrayList<Region>();
		records = new ArrayList<Record>();
	}

	public Species(String scientificName, String order, String superClass, String speciesName) {
		this.scientificName = scientificName;
		this.order = order;
		this.superClass = superClass;
		this.speciesName = speciesName;
		nbReportsByRegion = new ArrayList<Region>();
		records = new ArrayList<Record>();
	}

	public void addRecord(Record rec) {
		records.add(rec);
	}
	
	public void addRegion(Region reg) {
		nbReportsByRegion.add(reg);
	}
	
	public String getScientificName() {
		return scientificName;
	}

	public String getOrder() {
		return order;
	}

	public String getSuperClass() {
		return superClass;
	}

	public String getSpeciesName() {
		return speciesName;
	}
	

	public int getMaxOccurrence() {
		return maxOccurrence;
	}

	public int getMinOccurrence() {
		return minOccurrence;
	}

	public ArrayList<Region> getNbReportsByRegion() {
		return nbReportsByRegion;
	}

	public ArrayList<Record> getRecords() {
		return records;
	}
	
}
