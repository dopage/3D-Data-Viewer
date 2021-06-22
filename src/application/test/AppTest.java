package application.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.Test;

import application.common.Species;
import application.dataAcess.DataProvider;
import application.exceptions.UnknownSpeciesException;

class AppTest {

	@Test
	void initTest() {
		DataProvider dp = DataProvider.getInstance();
		assertNotEquals(null, dp);
	}
	
	@Test
	void getScientificNamesBeginWithTest() {
		DataProvider dp = DataProvider.getInstance();
		// Test du nombre de résultats maximum
		assertEquals(20, dp.getScientificNamesBeginWith("a").size());
		// Test des résultats
		String[] answer = {"Argentiniformes","Argentinidae"};
		ArrayList<String> res = dp.getScientificNamesBeginWith("argentini");
		for (int i = 0; i < answer.length; i++) {
			assertEquals(true, res.contains(answer[i]));
		}
	}
	
	@Test
	void getNbReportsByRegionFromFileTest() {
		// Test la lecture de données depuis une source local
		DataProvider dp = DataProvider.getInstance();
		// Test détection d'une espèce inconnue
		boolean espece_inconnue = false;
		try {
			Species s = dp.getNbReportsByRegionFromFile("data/Selachii.json", "Selachii");
			assertEquals(4511, s.getNbReportsByRegion().size());
		} catch (UnknownSpeciesException e) {
			espece_inconnue = true;
		}
	}
	
	@Test
	void getNbReportsByRegionTest() {
		DataProvider dp = DataProvider.getInstance();
		// Test détection d'une espèce inconnue
		boolean espece_inconnue = false;
		try {
			dp.getNbReportsByRegion("espece_inconnue");
		} catch (UnknownSpeciesException e) {
			espece_inconnue = true;
		}
		assertEquals(true, espece_inconnue);
		
		// Test si une espèce existante est bien reconnue
		boolean espece_existante = true;
		try {
			dp.getNbReportsByRegion("caretta");
		} catch (UnknownSpeciesException e) {
			espece_existante = false;
			e.printStackTrace();
		}
		assertEquals(true, espece_existante);
		
		// Test des données récupérées
		try {
			Species s = dp.getNbReportsByRegion("caretta");
			assertEquals("caretta", s.getScientificName());
			assertEquals(0, s.getMinOccurrence());
			assertEquals(5973, s.getMaxOccurrence());
			assertEquals(3323, s.getNbReportsByRegion().size());
		} catch (UnknownSpeciesException e) {
			e.printStackTrace();
		}

		// Test des données récupérées entre deux dates
		try {
			Calendar calendar = Calendar.getInstance();
	        calendar.set(2000, 0, 1);
	        Date from = calendar.getTime();
	        calendar.set(2005, 0, 1);
	        Date to = calendar.getTime();
	        Species s = dp.getNbReportsByRegion("caretta", from, to);
			assertEquals("caretta", s.getScientificName());
			assertEquals(0, s.getMinOccurrence());
			assertEquals(486, s.getMaxOccurrence());
			assertEquals(1151, s.getNbReportsByRegion().size());
		} catch (UnknownSpeciesException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void getNbReportsByRegionByTimeIntervalTest() {
		DataProvider dp = DataProvider.getInstance();
		// Test des données récupérées pour chaque interval de temps
		try {
			Calendar calendar = Calendar.getInstance();
	        calendar.set(2000, 0, 1);
	        Date from = calendar.getTime();
	        int nbIntervals = 3;
			ArrayList<Species> speciesArr = dp.getNbReportsByRegionByTimeInterval("caretta", from, 5, nbIntervals);
			assertEquals(nbIntervals, speciesArr.size());
			int minAnwsers[] = {0, 0, 0};
			int maxAnswers[] = {486, 1239, 3164};
			int nbRegionsAnswers[] = {1151, 1708, 1747};
			for (int i = 0; i < speciesArr.size(); i++) {
				Species s = speciesArr.get(i);
				assertEquals("caretta", s.getScientificName());
				assertEquals(minAnwsers[i], s.getMinOccurrence());
				assertEquals(maxAnswers[i], s.getMaxOccurrence());
				assertEquals(nbRegionsAnswers[i], s.getNbReportsByRegion().size());
			}
		} catch (UnknownSpeciesException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void getDetailsRecordsTest() {
		DataProvider dp = DataProvider.getInstance();
		// Test détection d'une espèce inconnue
		boolean espece_inconnue = false;
		try {
			dp.getDetailsRecords("spd", "espece_inconnue");
		} catch (UnknownSpeciesException e) {
			espece_inconnue = true;
		}
		assertEquals(true, espece_inconnue);
		
		// Test si une espèce existante est bien reconnue
		boolean espece_existante = true;
		try {
			dp.getDetailsRecords("spd", "caretta");
		} catch (UnknownSpeciesException e) {
			espece_existante = false;
			e.printStackTrace();
		}
		assertEquals(true, espece_existante);
		
		// Test des données récupérées en précisant un nom scientifique
		try {
			Species s = dp.getDetailsRecords("ezv", "cnidaria");
			assertEquals(10, s.getRecords().size());
			if (s.getRecords().size() > 0) {
				assertEquals("1971-06", s.getRecords().get(0).getEventDate());
				assertEquals("Bouillon Jean (Prof.)", s.getRecords().get(0).getRecorderBy());
				assertEquals(68884, s.getRecords().get(0).getShoreDistance());
				assertEquals(3554, s.getRecords().get(0).getBathymetry());
			}
		} catch (UnknownSpeciesException e) {
			e.printStackTrace();
		}
		
		// Test des données récupérées en précisant seulement un geohash		
		ArrayList<Species> species = dp.getDetailsRecords("xcr");
		assertEquals(1, species.size());
		if (species.size() > 0) {
			Species s = species.get(0);
			assertEquals("Dermochelys coriacea", s.getScientificName());
			assertEquals("2005-11-09T00:00:00", s.getRecords().get(0).getEventDate());
			assertEquals("Inconnu", s.getRecords().get(0).getRecorderBy());
			assertEquals(829312, s.getRecords().get(0).getShoreDistance());
			assertEquals(5307, s.getRecords().get(0).getBathymetry());
		}
	}
}
