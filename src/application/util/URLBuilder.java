package application.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Classe permettant de construire une URL en y ajoutant de nouveaux paramètres au fure et à mesure
 */
public class URLBuilder {

	private StringBuilder url;
	
	/**
	 * Constructeur de la classe URLBuilder
	 * @param url : l'adresse web comportant le nom de domaine et le dossier/fichier (exemple : http://api.domain.com/dir/")
	 */
	public URLBuilder(String url) {
		this.url = new StringBuilder();
		this.url.append(url);
	}
	
	/**
	 * Ajout un nouveau paramètre à l'url en encodant la valeur
	 * @param key : le nom du paramètre
	 * @param value : la valeur du paramètre
	 */
	public void addParameter(String key, String value) {
		url.append("&");
		url.append(key);
		url.append("=");
		try {
			url.append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Renvoie la chaine de caractères représentant l'URL
	 * @return : l'url
	 */
	public String getUrl() {
		return url.toString();
	}
}
