package com.interoperabilite.demo.Controller;

import com.interoperabilite.demo.Model.ArtistInfo;
import com.interoperabilite.demo.Repository.ArtistInfoRepository;
import com.interoperabilite.demo.Service.WikipediaExtractionService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WikipediaExtractionController {

    private final WikipediaExtractionService wikipediaExtractionService;
    private final ArtistInfoRepository artistInfoRepository; // Supposons que cela existe pour le stockage des données

    public WikipediaExtractionController(WikipediaExtractionService wikipediaExtractionService,
            ArtistInfoRepository artistInfoRepository) {
        this.wikipediaExtractionService = wikipediaExtractionService;
        this.artistInfoRepository = artistInfoRepository;
    }

    @PostMapping("/upload/wiki")
    public String handleWikiExtract(@RequestParam("pageTitle") String pageTitle,
            RedirectAttributes redirectAttributes) {
        ArtistInfo artistInfo = wikipediaExtractionService.extractArtistInfoFromWikipedia(pageTitle);
        if (artistInfo != null) {
            artistInfoRepository.save(artistInfo);
            redirectAttributes.addFlashAttribute("message", "Data extracted successfully from Wikipedia!");
            return "redirect:/uploadSuccess";
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to extract data from Wikipedia.");
            return "redirect:/uploadFail";
        }

    }
}
