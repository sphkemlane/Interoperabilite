package com.interoperabilite.demo.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.interoperabilite.demo.Model.Album;
import com.interoperabilite.demo.Model.ArtistInfo;
import com.interoperabilite.demo.Repository.AlbumRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class DatabaseExtractionService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WikipediaExtractionService wikipediaExtractionService;

    @Autowired
    private AlbumRepository albumRepository; // Injection de AlbumRepository

    @Transactional
    public void executeSqlScript(MultipartFile sqlFile) throws IOException {
        String sqlScript = new String(sqlFile.getBytes(), StandardCharsets.UTF_8);
        String[] sqlInstructions = sqlScript.split(";");

        for (String instruction : sqlInstructions) {
            if (!instruction.trim().isEmpty()) {
                entityManager.createNativeQuery(instruction).executeUpdate();
            }
        }
        // L'appel pour mettre à jour les albums avec les ID Wikidata est correct ici.
        updateAlbumsWithWikidataIds();
    }
    @Transactional
    public void updateAlbumsWithWikidataIds() {
        List<Album> albums = albumRepository.findAll(); // Récupère tous les albums
    
        for (Album album : albums) {
            if ((album.getWikidataId() == null || album.getWikidataId().isEmpty()) || 
                (album.getDbpediaId() == null || album.getDbpediaId().isEmpty())) {
                // Supposons que extractArtistInfoFromWikipedia retourne maintenant aussi le dbpediaId
                ArtistInfo artistInfo = wikipediaExtractionService.extractArtistInfoFromWikipedia(album.getArtist());
                if (artistInfo != null) {
                    if (artistInfo.getWikidataId() != null) {
                        album.setWikidataId(artistInfo.getWikidataId());
                    }
                    if (artistInfo.getDbpediaId() != null) { // Supposons qu'il y a maintenant un getDbpediaId
                        album.setDbpediaId(artistInfo.getDbpediaId());
                    }
                    albumRepository.save(album); // Sauvegarde de l'album mis à jour
                }
            }
        }
    }
}    