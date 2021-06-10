package application.dataAcess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import application.common.Record;
import application.common.Region;
import application.common.Species;
import javafx.geometry.Point2D;
import application.util.JSONHelper;
import application.util.URLBuilder;

public class DataProvider implements DataProviderInterface {

	private static DataProvider instance = null;
	
	/**
	 * Récupère l'instance de DataProvider (singleton).
	 * @return L'instance DataProvider
	 */
	public static DataProvider getInstance() {
		if(instance == null) instance = new DataProvider();
		return instance;
	}
	
	@Override
	public Species getNbReportsByRegion(String scientificName) {
		return getNbReportsByRegion(scientificName, null, null);
	}
 
	@Override
	public Species getNbReportsByRegion(String scientificName, Date from, Date to) {
		Species species = new Species();
		int minOccurence = 0;
		int maxOccurence = 0;
		String url = "https://api.obis.org/v3/occurrence/grid/3?";
		if (scientificName != null) {
			url += "scientificname=" + scientificName;
		}
		if (from != null && to != null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			url += "&startdate=" + formatter.format(from) + "&enddate=" + formatter.format(to);
		}
		try {
			JSONObject jsonRoot = new JSONObject(JSONHelper.readJsonFromUrl(url));
			JSONArray listeDesRegions = jsonRoot.getJSONArray("features");
			System.out.println("nb regions : " + listeDesRegions.length());
			for (int i = 0; i < listeDesRegions.length(); i++) {
				ArrayList<Point2D> points = new ArrayList<Point2D>();
				JSONArray coords = listeDesRegions.getJSONObject(i).getJSONObject("geometry").getJSONArray("coordinates").getJSONArray(0);
				for (int j = 0; j < coords.length(); j++) {
					Point2D p = new Point2D(coords.getJSONArray(j).getDouble(0), coords.getJSONArray(j).getDouble(1));
					points.add(p);
				}
				int nbReports = listeDesRegions.getJSONObject(i).getJSONObject("properties").getInt("n");
				if (nbReports > maxOccurence)
					maxOccurence = nbReports;
				else if (nbReports < minOccurence)
					minOccurence = nbReports;
				Region region = new Region(points, nbReports);
				species.addRegion(region);
			}
			species.setMinOccurrence(minOccurence);
			species.setMaxOccurrence(maxOccurence);
		}
		catch (JSONException e) {
			System.err.println("Erreur dans le json - getNbReportsByRegion()");
			//e.printStackTrace();
		}
		return species;
	}

	@Override
	public ArrayList<Species> getNbReportsByZoneByTimeInterval(String scientificName, String geoHash, Date from, int timeInterval, int nbIntervals) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getScientificNamesBeginWith(String beginWith) {
		ArrayList<String> names = new ArrayList<String>();
		String url = "https://api.obis.org/v3/taxon/complete/verbose/" + URLBuilder.encode(beginWith);
		try {
			JSONArray jsonRoot = new JSONArray(JSONHelper.readJsonFromUrl(url));
			for (int i = 0; i < jsonRoot.length(); i++) {
				names.add(jsonRoot.getJSONObject(i).getString("scientificName"));
			}	
		}
		catch (JSONException e) {
			System.err.println("Erreur dans le json - getScientificNamesBeginWith()");
			//e.printStackTrace();
		}
		return names;
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
