package com.interoperabilite.demo.Service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.interoperabilite.demo.Model.ArtistInfo;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class WikipediaExtractionService {

    private final RestTemplate restTemplate;

    @Autowired
    public WikipediaExtractionService(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    public ArtistInfo extractArtistInfoFromWikipedia(String pageTitle) {
        try {
            // Ensure the pageTitle is just the title, not a URL

            String safeTitle = pageTitle.replace(" ", "_");
            String apiUrl = String.format(
                    "https://fr.wikipedia.org/w/api.php?action=query&titles=%s&prop=revisions&rvprop=content&format=json",
                    safeTitle);

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            System.out.println("Requesting URL: " + apiUrl);

            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                String htmlContent = response.getBody();
                Document doc = Jsoup.parse(htmlContent);

                String birthDate = doc.select("th:contains(Naissance) + td").text();
                String realName = doc.select("th:contains(Nom de naissance) + td").text();
                String nationality = doc.select("th:contains(Nationalité) + td").text();
                String occupation = doc.select("th:contains(Activités) + td").text();
                String label = doc.select("th:contains(Label) + td").text();
                String artisticGenre = doc.select("th:contains(Genre artistique) + td").text();

                return new ArtistInfo(birthDate, realName, nationality, occupation, label, artisticGenre);

            } else {
                // Handle non-successful response or missing body
                System.out.println(
                        "Received non-success response from Wikipedia or body is missing for page: " + pageTitle);
                return null; // or throw a custom exception
            }
        } catch (HttpClientErrorException e) {
            // Handle the 404 Not Found exception specifically
            System.err.println("Wikipedia page not found for title: " + pageTitle + ", error: " + e.getMessage());
            return null; // or throw a custom exception
        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("An error occurred while extracting information from Wikipedia for title: " + pageTitle
                    + ", error: " + e.getMessage());
            return null; // or throw a custom exception
        }
    }

}
