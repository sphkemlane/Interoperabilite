package com.interoperabilite.demo.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import com.interoperabilite.demo.Model.ArtistInfo;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WikipediaExtractionService {

    private final RestTemplate restTemplate;

    @Autowired
    public WikipediaExtractionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ArtistInfo extractArtistInfoFromWikipedia(String pageTitle) {
        try {
            String titleOfPage = pageTitle.substring(pageTitle.lastIndexOf("/") + 1);
            String safeTitle = URLEncoder.encode(titleOfPage, StandardCharsets.UTF_8.toString());
            String apiUrl = "https://fr.wikipedia.org/w/api.php?action=parse&page=" + safeTitle
                    + "&prop=text|pageprops&format=json";

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            String wikidataId = "";
            String dbpediaTitle = titleOfPage.replace(" ", "_");
            String dbpediaId = "http://fr.dbpedia.org/resource/" + dbpediaTitle;

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                JSONObject jsonResponse = new JSONObject(response.getBody());

                if (!jsonResponse.has("parse")) {
                    System.err.println("No 'parse' key found in the JSON response for the page: " + safeTitle);
                    return null;
                }

                JSONObject parse = jsonResponse.getJSONObject("parse");
                String htmlContent = parse.getJSONObject("text").getString("*");
                Document doc = Jsoup.parse(htmlContent);

                // Extraction des informations
                String birthDate = doc.select("th:contains(Naissance) + td").text();
                String realName = doc.select("th:contains(Nom de naissance) + td").text();
                String nationality = doc.select("th:contains(Nationalité) + td").text();
                String occupation = doc.select("th:contains(Activités) + td").text();
                String label = doc.select("th:contains(Label) + td").text();
                String artisticGenre = doc.select("th:contains(Genre artistique) + td").text();

                // Dans la méthode extractArtistInfoFromWikipedia, après la vérification de
                // pageprops :
                if (wikidataId.isEmpty()) {
                    System.err.println("Attempting to fetch Wikidata ID using Wikidata search API for: " + safeTitle);
                    wikidataId = fetchWikidataId(titleOfPage);
                    if (!wikidataId.isEmpty()) {
                        System.out.println("Found Wikidata ID using search API: " + wikidataId);
                    }
                }
                String formattedTitle = titleOfPage.replace(" ", "_");
                dbpediaId = formattedTitle;

                // Création de l'objet ArtistInfo avec toutes les informations extraites
                return new ArtistInfo(birthDate, realName, nationality, occupation, label, artisticGenre, wikidataId,
                        dbpediaId);
            } else {
                System.out.println("Response from Wikipedia was not successful for page: " + pageTitle);
                return null;
            }
        } catch (Exception e) {
            System.err.println("An error occurred while extracting information from Wikipedia for title: " + pageTitle
                    + ", error: " + e.getMessage());
            return null;
        }
    }

    private String fetchWikidataId(String titleOfPage) {
        try {
            String safeTitle = URLEncoder.encode(titleOfPage, StandardCharsets.UTF_8.toString());
            String wikidataSearchUrl = String.format(
                    "https://www.wikidata.org/w/api.php?action=wbsearchentities&search=%s&language=fr&format=json",
                    safeTitle);

            ResponseEntity<String> response = restTemplate.getForEntity(wikidataSearchUrl, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                JSONObject jsonResponse = new JSONObject(response.getBody());
                JSONArray searchResults = jsonResponse.getJSONArray("search");
                if (searchResults.length() > 0) {
                    JSONObject firstResult = searchResults.getJSONObject(0);
                    return firstResult.getString("id");
                } else {
                    System.err.println("No Wikidata ID found for " + titleOfPage);
                }
            }
        } catch (Exception e) {
            System.err
                    .println("An error occurred while fetching Wikidata ID for " + titleOfPage + ": " + e.getMessage());
        }
        return ""; // Retourne une chaîne vide si aucun ID n'est trouvé ou en cas d'erreur
    }

}