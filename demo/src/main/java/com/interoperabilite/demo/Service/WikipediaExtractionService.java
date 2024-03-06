package com.interoperabilite.demo.Service;

import java.nio.charset.StandardCharsets;

import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interoperabilite.demo.Model.Album;
import com.interoperabilite.demo.Repository.AlbumRepository;

@Service
public class WikipediaExtractionService {

    private final RestTemplate restTemplate;
    private final AlbumRepository albumRepository; // Repositoire pour enregistrer les données

    @Autowired
    public WikipediaExtractionService(RestTemplateBuilder restTemplateBuilder, AlbumRepository albumRepository) {
        this.restTemplate = restTemplateBuilder.build();
        this.albumRepository = albumRepository;
    }

    public void extractAndStoreWikiInfo(String pageTitle) {
        String encodedPageTitle = new org.apache.catalina.util.URLEncoder().encode(pageTitle, StandardCharsets.UTF_8);
        String wikiApiUrl = "https://en.wikipedia.org/api/rest_v1/page/summary/" + encodedPageTitle;

        // Faire l'appel au service Wikipedia
        ResponseEntity<String> response = restTemplate.getForEntity(wikiApiUrl, String.class);
        String wikidataId = parseWikiResponse(response.getBody());

        // Stocker l'information extraite dans la base de données
        Album album = new Album();
        album.setWikidataId(wikidataId);
        // Ajoutez d'autres informations au besoin
        albumRepository.save(album);
    }

    private String parseWikiResponse(String responseBody) {
        try {
            // Convertir la réponse JSON en objet Java avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(responseBody);

            // Obtenir le Wikidata ID de l'objet JSON
            // Note : le chemin exact pour accéder à l'ID Wikidata dépend de la structure de
            // la réponse de l'API
            JsonNode wikiDataIdNode = rootNode.path("wikibase_item");
            return wikiDataIdNode.asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
