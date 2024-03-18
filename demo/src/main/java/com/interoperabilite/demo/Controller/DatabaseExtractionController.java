package com.interoperabilite.demo.Controller;

import com.interoperabilite.demo.Service.DatabaseExtractionService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DatabaseExtractionController {

    private final DatabaseExtractionService databaseExtractionService;

    public DatabaseExtractionController(DatabaseExtractionService databaseExtractionService) {
        this.databaseExtractionService = databaseExtractionService;
    }

    @PostMapping("/upload/sql")
    public String handleSqlUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a SQL file to upload.");
            return "redirect:/uploadForm";
        }
        try {
            databaseExtractionService.executeSqlScript(file);
            redirectAttributes.addFlashAttribute("message", "SQL script executed successfully!");
            return "redirect:/uploadSuccess";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "Failed to execute SQL script: " + e.getMessage());
            return "redirect:/uploadForm";
        }
    }

    @GetMapping("/uploadsqlSuccess")
    public String uploadSuccess() {
        return "uploadSuccess";

    }
}
