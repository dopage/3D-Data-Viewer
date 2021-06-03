package application.dataAcess;

import java.util.ArrayList;
import java.util.Date;

import application.common.Species;
import javafx.geometry.Point2D;

public class DataProvider implements DataProviderInterface {

	@Override
	public Species getNbReportsByRegion(String scientificName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Species getNbReportsByRegion(String scientificName, Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Species> getNbReportsByZoneByTimeInterval(String scientificName, String geoHash, Date from, int timeInterval, int nbIntervals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getScientificNamesBeginWith(String beginWith) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String GPStoHash(Point2D CoordGPS) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Species getDetailsRecords(String geoHash, String scientificName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Species getDetailsRecords(String geoHash, String scientificName, Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Species> getDetailsRecords(String geoHash) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Species> getDetailsRecords(String geoHash, Date from, Date to) {
		// TODO Auto-generated method stub
		return null;
	}

}
