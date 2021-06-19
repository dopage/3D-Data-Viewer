package application.common;

public class Record {

	private String eventDate;
	private String recorderBy;
	private int shoreDistance;
	private int bathymetry;
	
	public Record(String eventDate, String recorderBy, int shoreDistance, int bathymetry) {
		super();
		this.eventDate = eventDate;
		this.recorderBy = recorderBy;
		this.shoreDistance = shoreDistance;
		this.bathymetry = bathymetry;
	}

	public String getEventDate() {
		return eventDate;
	}

	public int getShoreDistance() {
		return shoreDistance;
	}

	public int getBathymetry() {
		return bathymetry;
	}
	
	public String getRecorderBy() {
		return recorderBy;
	}
	
	@Override
	public String toString() {
		String str = "";
		
		return str;
	}
}
