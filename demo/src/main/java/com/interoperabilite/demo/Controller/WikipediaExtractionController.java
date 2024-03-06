package com.interoperabilite.demo.Controller;


import com.interoperabilite.demo.Service.WikipediaExtractionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WikipediaExtractionController {

    private final WikipediaExtractionService wikipediaExtractionService;

    @Autowired
    public WikipediaExtractionController(WikipediaExtractionService wikipediaExtractionService) {
        this.wikipediaExtractionService = wikipediaExtractionService;
    }

    @PostMapping("/upload/wiki")
    public String handleWikiExtract(@RequestParam("pageTitle") String pageTitle, RedirectAttributes redirectAttributes) {
        try {
            wikipediaExtractionService.extractAndStoreWikiInfo(pageTitle);
            redirectAttributes.addFlashAttribute("message", "Data extracted successfully from Wikipedia!");
            return "redirect:/uploadSuccess";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to extract data from Wikipedia: " + e.getMessage());
            return "redirect:/uploadFail";
        }
    }

    // Assurez-vous d'avoir une page 'uploadSuccess.html' et 'uploadFail.html' dans 'src/main/resources/templates'
}
