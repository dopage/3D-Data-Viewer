package application.common;

public class Record {

	private String eventDate;
	private String recorderBy;
	private Integer shoreDistance;
	private Integer bathymetry;
	
	public Record(String eventDate, String recorderBy, int shoreDistance, int bathymetry) {
		this.eventDate = eventDate;
		this.recorderBy = recorderBy;
		this.shoreDistance = shoreDistance;
		this.bathymetry = bathymetry;
	}

	public String getEventDate() {
		if (eventDate == null)
			return "Inconnu";
		return eventDate;
	}

	public String getRecorderBy() {
		if (recorderBy == null)
			return "Inconnu";
		return recorderBy;
	}
	
	public Integer getShoreDistance() {
		return shoreDistance;
	}

	public Integer getBathymetry() {
		return bathymetry;
	}
	
	
	@Override
	public String toString() {
		String str = "";
		
		return str;
	}
}
