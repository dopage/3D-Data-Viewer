package application.dataAcess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
		species.setScientificName(scientificName);
		int minOccurence = 0;
		int maxOccurence = 0;
		URLBuilder url = new URLBuilder("https://api.obis.org/v3/occurrence/grid/3?");
		if (scientificName != null && !scientificName.equals("")) {
			url.addParameter("scientificname", scientificName);
			if (from != null && to != null) {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				url.addParameter("startdate", formatter.format(from));
				url.addParameter("&enddate", formatter.format(to));
			}
			try {
				JSONObject jsonRoot = new JSONObject(JSONHelper.readJsonFromUrl(url.getUrl()));
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
		}
		return species;
	}

	@Override
	public ArrayList<Species> getNbReportsByRegionByTimeInterval(String scientificName, String geoHash, Date from, int intervalDuration, int nbIntervals) {
		ArrayList<Species> species = new ArrayList<Species>();
		if (scientificName != null && !scientificName.equals("") && from != null) {
			// Pour chaque interval de temps, on effectue une requête afin de récupérer les signalements
			for (int i = 0; i < nbIntervals; i++) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(from);
				calendar.add(Calendar.YEAR, intervalDuration * i);
				Date intervalFrom = calendar.getTime();
				calendar.add(Calendar.YEAR, intervalDuration);
				Date intervalTo = calendar.getTime();
				species.add(this.getNbReportsByRegion(scientificName, intervalFrom, intervalTo));
			}
		}
		// Pour résumer, le tableau "species" va être de taille "nbIntervals" et va contenir
		// des instances de la classe "Species" pour des intervalles de temps différents 
		return species;
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
		return getDetailsRecords(geoHash, scientificName, null, null);
	}

	@Override
	public Species getDetailsRecords(String geoHash, String scientificName, Date from, Date to) {
		Species species = new Species();
		int minOccurence = 0;
		int maxOccurence = 0;
		URLBuilder url = new URLBuilder("https://api.obis.org/v3/occurrence?");
		url.addParameter("geometry", geoHash);
		if (scientificName != null) {
			url.addParameter("scientificname", scientificName);
			species.setScientificName(scientificName);
		}
		if (from != null && to != null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			url.addParameter("startdate=", formatter.format(from));
			url.addParameter("&enddate=", formatter.format(to));
		}
		try {
			JSONObject jsonRoot = new JSONObject(JSONHelper.readJsonFromUrl(url.getUrl()));
			JSONArray listeDesRecords = jsonRoot.getJSONArray("results");
			System.out.println("nb records : " + listeDesRecords.length());
			for (int i = 0; i < listeDesRecords.length(); i++) {
				JSONObject recordJSON = listeDesRecords.getJSONObject(i);
				if (i == 0) {
					if (!recordJSON.isNull("order"))
						species.setOrder(recordJSON.getString("order"));
					if (!recordJSON.isNull("species"))
						species.setSpeciesName(recordJSON.getString("species"));
					if (!recordJSON.isNull("superclass"))
						species.setSuperClass(recordJSON.getString("superclass"));
				}
				String recordedBy = null;
				if (!recordJSON.isNull("recordedBy"))
					recordedBy = recordJSON.getString("recordedBy");
				Record record = new Record(recordedBy);
				species.addRecord(record);
			}
			species.setMinOccurrence(minOccurence);
			species.setMaxOccurrence(maxOccurence);
		}
		catch (JSONException e) {
			System.err.println("Erreur dans le json - getDetailsRecords()");
			//e.printStackTrace();
		}
		return species;
	}

	@Override
	public ArrayList<Species> getDetailsRecords(String geoHash) {
		return getDetailsRecords(geoHash, null, null);
	}

	@Override
	public ArrayList<Species> getDetailsRecords(String geoHash, Date from, Date to) {
		ArrayList<Species> species = new ArrayList<Species>();
		URLBuilder url = new URLBuilder("https://api.obis.org/v3/occurrence?");
		url.addParameter("geometry", geoHash);
		if (from != null && to != null) {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			url.addParameter("startdate=", formatter.format(from));
			url.addParameter("&enddate=", formatter.format(to));
		}
		try {
			JSONObject jsonRoot = new JSONObject(JSONHelper.readJsonFromUrl(url.getUrl()));
			JSONArray listeDesRecords = jsonRoot.getJSONArray("results");
			System.out.println("nb records : " + listeDesRecords.length());
			for (int i = 0; i < listeDesRecords.length(); i++) {
				JSONObject recordJSON = listeDesRecords.getJSONObject(i);
				// Pour chaque report, on récupère le nom scientifique de l'espèce
				String scientificName = null;
				if (!recordJSON.isNull("scientificName"))
					scientificName = recordJSON.getString("scientificName");
				if (scientificName != null && !scientificName.equals("")) {
					// On vérifie si l'espèce fait déjà partie de la liste de nos espèces dans le tableau "species"
					boolean espece_existante = false;
					for (int j = 0; j < species.size() && !espece_existante; j++) {
						// Si une instance existe déjà pour représenter cette l'espèce, il suffit d'ajoute le "record" à sa liste
						if (scientificName.equals(species.get(j).getScientificName())) {
							espece_existante = true;
							String recordedBy = null;
							if (!recordJSON.isNull("recordedBy"))
								recordedBy = recordJSON.getString("recordedBy");
							Record record = new Record(recordedBy);
							species.get(j).addRecord(record);
						}
					}
					// Si aucune instance n'existe pour cette espèce, alors on en créée une
					if (!espece_existante) {
						Species s = new Species();
						s.setScientificName(scientificName);
						if (!recordJSON.isNull("order"))
							s.setOrder(recordJSON.getString("order"));
						if (!recordJSON.isNull("species"))
							s.setSpeciesName(recordJSON.getString("species"));
						if (!recordJSON.isNull("superclass"))
							s.setSuperClass(recordJSON.getString("superclass"));
						String recordedBy = null;
						if (!recordJSON.isNull("recordedBy"))
							recordedBy = recordJSON.getString("recordedby");
						Record record = new Record(recordedBy);
						s.addRecord(record);
						species.add(s);
					}
				}
			}
		}
		catch (JSONException e) {
			System.err.println("Erreur dans le json - getDetailsRecords()");
			//e.printStackTrace();
		}
		return species;
	}

}
