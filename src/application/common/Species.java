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
	
	public Species() {
		nbReportsByRegion = new ArrayList<Region>();
		records = new ArrayList<Record>();
	}

	public void addRecord(Record rec) {
		records.add(rec);
	}
	
	public void addRegion(Region reg) {
		nbReportsByRegion.add(reg);
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("species: " + speciesName);
		str.append(" | scientificName: " + scientificName);
		str.append(" | order: " + order);
		str.append(" | superClass: " + superClass);
		str.append(" | nbRecords: " + records.size());
		str.append(" | nbRegions: " + nbReportsByRegion.size());
		return str.toString();
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

	public void setSpeciesName(String speciesName) {
		this.speciesName = speciesName;
	}

	public void setScientificName(String scientificName) {
		this.scientificName = scientificName;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public void setSuperClass(String superClass) {
		this.superClass = superClass;
	}

	public void setMaxOccurrence(int maxOccurrence) {
		this.maxOccurrence = maxOccurrence;
	}

	public void setMinOccurrence(int minOccurrence) {
		this.minOccurrence = minOccurrence;
	}
}
