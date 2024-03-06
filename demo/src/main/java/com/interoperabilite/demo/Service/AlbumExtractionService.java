package com.interoperabilite.demo.Service;


import com.interoperabilite.demo.Model.Album;
import com.interoperabilite.demo.Repository.AlbumRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class AlbumExtractionService {

    private final AlbumRepository albumRepository;
    private final RestTemplate restTemplate;
    private static final String API_URL = "http://external.api/albums";

    public AlbumExtractionService(AlbumRepository albumRepository, RestTemplate restTemplate) {
        this.albumRepository = albumRepository;
        this.restTemplate = restTemplate;
    }

    public void extractAndSaveAlbums() {
        Album[] albums = restTemplate.getForObject(API_URL, Album[].class);
        if (albums != null) {
            for (Album album : albums) {
                albumRepository.save(album);
            }
        }
    }
}
