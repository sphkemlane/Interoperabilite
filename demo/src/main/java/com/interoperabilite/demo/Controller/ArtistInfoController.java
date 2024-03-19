package com.interoperabilite.demo.Controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.interoperabilite.demo.Model.ArtistInfo;
import com.interoperabilite.demo.Repository.ArtistInfoRepository;

@Controller
public class ArtistInfoController {

    private final ArtistInfoRepository artistInfoRepository;

    public ArtistInfoController(ArtistInfoRepository artistInfoRepository) {
        this.artistInfoRepository = artistInfoRepository;
    }

    @GetMapping("/artists")
    public String showArtistsPage(Model model) {
        List<ArtistInfo> artists = artistInfoRepository.findAll();
        model.addAttribute("artists", artists);
        return "artists"; // Nom du fichier Thymeleaf pour la vue des artistes
    }
}
