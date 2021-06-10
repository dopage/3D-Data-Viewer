package application.dataAcess;

import java.util.ArrayList;
import java.util.Date;

import application.common.Species;
import javafx.geometry.Point2D;

public interface DataProviderInterface {
	
	/**
	 * Récupère le nombre de signalements effectués dans chaque région et pour une espèce en particulier
	 * @param scientificName : le nom scientifique de l'espèce
	 * @return : une instance de la classe "species" avec la liste des signalements présente dans son attribut "nbReportsByRegion"
	 */
	public Species getNbReportsByRegion(String scientificName);

	/**
	 * Récupère le nombre de signalements effectués dans chaque région et pour une espèce en particulier entre deux dates
	 * @param scientificName : le nom scientifique de l'espèce
	 * @param from : la date minimal des signalements
	 * @param to : la date maximal des signalements
	 * @return : une instance de la classe "species" avec la liste des signalements présente dans son attribut "nbReportsByRegion"
	 */
	public Species getNbReportsByRegion(String scientificName, Date from, Date to);

	// retourne un tableau de taille nbIntervals contenant l'espèce et ses signalements par région pour chaque intervalle (exemple : l'élément 3 du tableau contient les signalements de l'espèce pour le 3ème pas de temps)
	public ArrayList<Species> getNbReportsByZoneByTimeInterval(String scientificName, String geoHash, Date from, int timeInterval, int nbIntervals);
	
	/**
	 * Récupère les premiers noms des espèces commençant par une chaîne de caractères passée en paramètre
	 * @param beginWith : le début du nom de l'espèce
	 * @return : la liste des noms d'espèces correspondant
	 */
	public ArrayList<String> getScientificNamesBeginWith(String beginWith);

	public String GPStoHash(Point2D CoordGPS);

	/**
	 * Récupère les enregistrements effectués pour une espèce en particulier
	 * @param geoHash : le GeoHash à utiliser
	 * @param scientificName : le nom scientifique de l'espèce
	 * @return : une instance de la classe "species" avec la liste des enregistrements présente dans son attribut "records"
	 */
	public Species getDetailsRecords(String geoHash, String scientificName);

	/**
	 * Récupère les enregistrements effectués pour une espèce en particulier entre deux dates
	 * @param geoHash : le GeoHash à utiliser
	 * @param scientificName : le nom scientifique de l'espèce
	 * @param from : la date minimal des enregistrements
	 * @param to : la date maximal des enregistrements
	 * @return : une instance de la classe "species" avec la liste des enregistrements présente dans son attribut "records"
	 */
	public Species getDetailsRecords(String geoHash, String scientificName, Date from, Date to);

	/**
	 * Récupère les enregistrements effectués
	 * @param geoHash : le GeoHash à utiliser
	 * @return : une liste d'instances de la classe "species" avec la liste des enregistrements présente dans leur attribut "records"
	 */
	public ArrayList<Species> getDetailsRecords(String geoHash);

	/**
	 * Récupère les enregistrements effectués entre deux dates
	 * @param geoHash : le GeoHash à utiliser
	 * @param from : la date minimal des enregistrements
	 * @param to : la date maximal des enregistrements
	 * @return : une liste d'instances de la classe "species" avec la liste des enregistrements présente dans leur attribut "records"
	 */
	public ArrayList<Species> getDetailsRecords(String geoHash, Date from, Date to);
}
