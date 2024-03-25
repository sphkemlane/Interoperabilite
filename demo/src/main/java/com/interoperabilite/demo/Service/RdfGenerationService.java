package com.interoperabilite.demo.Service;

import com.interoperabilite.demo.Model.Album;
import com.interoperabilite.demo.Model.ArtistInfo;
import com.interoperabilite.demo.Repository.AlbumRepository;
import com.interoperabilite.demo.Repository.ArtistInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Service
public class RdfGenerationService {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistInfoRepository artistInfoRepository;

    private static final String FILE_PATH = System.getProperty("user.home") + "/Desktop/myProjectRDF.ttl";

    @Scheduled(fixedRate = 30000) // Exécuter cette méthode toutes les 30 secondes
    public void generateAndUpdateRdfFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            writer.write("@prefix : <http://interoperabilite.com/> .\n");
            writer.write("@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n");
            writer.write("@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n\n");

            List<Album> albums = albumRepository.findAll();
            for (Album album : albums) {
                String albumResource = String.format(":Album%d ", album.getId());
                writer.write(albumResource + "rdf:type :Album ;\n");
                writer.write("    rdfs:label \"" + album.getTitle() + "\"^^<http://www.w3.org/2001/XMLSchema#string> ;\n");
                writer.write("    :hasArtist \"" + album.getArtist() + "\" ;\n");
                writer.write("    :releaseYear \"" + album.getReleaseYear() + "\"^^<http://www.w3.org/2001/XMLSchema#integer> .\n\n");
            }

            List<ArtistInfo> artists = artistInfoRepository.findAll();
            for (ArtistInfo artist : artists) {
                String artistResource = String.format(":Artist%d ", artist.getId());
                writer.write(artistResource + "rdf:type :Artist ;\n");
                writer.write("    rdfs:label \"" + artist.getRealName() + "\"^^<http://www.w3.org/2001/XMLSchema#string> .\n\n");
            }

            System.out.println("RDF file updated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing RDF to file: " + e.getMessage());
        }
    }
}