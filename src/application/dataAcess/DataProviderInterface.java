package application.dataAcess;

import java.util.ArrayList;
import java.util.Date;

import application.common.Species;
import javafx.geometry.Point2D;

public interface DataProviderInterface {
	
	public Species getNbReportsByRegion(String scientificName);

	public Species getNbReportsByRegion(String scientificName, Date from, Date to);

	// retourne un tableau de taille nbIntervals contenant l'espèce et ses signalements par région pour chaque intervalle (exemple : l'élément 3 du tableau contient les signalements de l'espèce pour le 3ème pas de temps)
	public ArrayList<Species> getNbReportsByZoneByTimeInterval(String scientificName, String geoHash, Date from, int timeInterval, int nbIntervals);
	
	/**
	 * Récupére les premiers noms des espèces commençant par une chaîne de caractères passée en paramètre
	 * @param beginWith : le début du nom de l'espèce
	 * @return : la liste des noms d'espèces correspondant
	 */
	public ArrayList<String> getScientificNamesBeginWith(String beginWith);

	public String GPStoHash(Point2D CoordGPS);

	public Species getDetailsRecords(String geoHash, String scientificName);

	public Species getDetailsRecords(String geoHash, String scientificName, Date from, Date to);

	// Cas où scientificName est null
	public ArrayList<Species> getDetailsRecords(String geoHash);

	// Cas où scientificName est null
	public ArrayList<Species> getDetailsRecords(String geoHash, Date from, Date to);
	
	//loadFromJson(String path)
}
