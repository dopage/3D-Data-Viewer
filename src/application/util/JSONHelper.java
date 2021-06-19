package application.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.concurrent.TimeUnit;


/**
 * Classe permettant de facilité l'importation de fichiers JSON
 */
public class JSONHelper {
	
	/**
	 * Lit un fichier json depuis un chemin d'accès
	 * @param path : le chemin d'accès et le nom du fichier json
	 * @return retourne le contenu du fichier json
	 */
	public static String readJsonFromFile(String path)
	{
		String json = "";
		try (Reader reader = new FileReader("data.json")) {
			BufferedReader rd = new BufferedReader(reader);
			json = JSONHelper.readAll(rd);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * Lit un fichier json depuis une adresse web
	 * @param url : l'url du fichier json
	 * @return retourne le contenu du fichier json
	 */
	public static String readJsonFromUrl(String url)
	{
		System.out.println("call url: " + url);
		String json = "";
		HttpClient client = HttpClient.newBuilder()
				.version(Version.HTTP_1_1)
				.followRedirects(Redirect.NORMAL)
				.connectTimeout(Duration.ofSeconds(20))
				.build();
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofMinutes(2))
				.header("Content-Type", "application/json")
				.GET()
				.build();
		try {
			json = client.sendAsync(request, BodyHandlers.ofString())
			.thenApply(HttpResponse::body).get(10,TimeUnit.SECONDS);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
}
