package application.dataAcess;

import java.util.ArrayList;
import java.util.Date;

import application.common.Species;
import application.exceptions.UnknownSpeciesException;

public interface DataProviderInterface {
	
	/**
	 * Récupère le nombre de signalements effectués dans chaque région et pour une espèce en particulier
	 * @param scientificName : le nom scientifique de l'espèce
	 * @return : une instance de la classe "species" avec la liste des signalements présente dans son attribut "nbReportsByRegion"
	 * @throws UnknownSpeciesException : une exception peut être lancée si l'espèce n'est pas répertoriée dans la base de données
	 */
	public Species getNbReportsByRegion(String scientificName) throws UnknownSpeciesException;
	
	/**
	 * Récupère le nombre de signalements effectués dans chaque région et pour une espèce en particulier entre deux dates
	 * @param scientificName : le nom scientifique de l'espèce
	 * @param from : la date minimal des signalements
	 * @param to : la date maximal des signalements
	 * @return : une instance de la classe "species" avec la liste des signalements présente dans son attribut "nbReportsByRegion"
	 * @throws UnknownSpeciesException : une exception peut être lancée si l'espèce n'est pas répertoriée dans la base de données
	 */
	public Species getNbReportsByRegion(String scientificName, Date from, Date to) throws UnknownSpeciesException;
	
	/**
	 * Récupère le nombre de signalements effectués dans chaque région et pour une espèce en particulier pour des intervalles de temps différents
	 * @param scientificName : le nom scientifique de l'espèce
	 * @param from : la date où débute le premier interval de temps
	 * @param intervalDuration : la durée en nombre d'années de chaque interval de temps
	 * @param nbIntervals : le nombre d'intervalles de temps
	 * @return : une liste d'instances de la classe "species" contenant les signalements de l'espèce à des intervalles de temps différents
	 * @throws UnknownSpeciesException : une exception peut être lancée si l'espèce n'est pas répertoriée dans la base de données
	 */
	public ArrayList<Species> getNbReportsByRegionByTimeInterval(String scientificName, Date from, int intervalDuration, int nbIntervals) throws UnknownSpeciesException;
	
	/**
	 * Récupère les premiers noms des espèces commençant par une chaîne de caractères passée en paramètre
	 * @param beginWith : le début du nom de l'espèce
	 * @return : la liste des noms d'espèces correspondant
	 */
	public ArrayList<String> getScientificNamesBeginWith(String beginWith);
	
	/**
	 * Récupère les enregistrements effectués pour une espèce en particulier
	 * @param geoHash : le GeoHash à utiliser
	 * @param scientificName : le nom scientifique de l'espèce
	 * @return : une instance de la classe "species" avec la liste des enregistrements présente dans son attribut "records"
	 * @throws UnknownSpeciesException : une exception peut être lancée si l'espèce n'est pas répertoriée dans la base de données
	 */
	public Species getDetailsRecords(String geoHash, String scientificName) throws UnknownSpeciesException;

	/**
	 * Récupère les enregistrements effectués pour une espèce en particulier entre deux dates
	 * @param geoHash : le GeoHash à utiliser
	 * @param scientificName : le nom scientifique de l'espèce
	 * @param from : la date minimal des enregistrements
	 * @param to : la date maximal des enregistrements
	 * @return : une instance de la classe "species" avec la liste des enregistrements présente dans son attribut "records"
	 * @throws UnknownSpeciesException : une exception peut être lancée si l'espèce n'est pas répertoriée dans la base de données
	 */
	public Species getDetailsRecords(String geoHash, String scientificName, Date from, Date to) throws UnknownSpeciesException;

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
