package com.interoperabilite.demo.Controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.interoperabilite.demo.Service.PdfExtractionService;

@Controller
public class PdfUploadController {

    private final PdfExtractionService PdfExtractionService;

    public PdfUploadController(PdfExtractionService PdfExtractionService) {
        this.PdfExtractionService = PdfExtractionService;
    }

    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handlePdfUpload(@RequestParam("pdfFile") MultipartFile file, RedirectAttributes redirectAttributes) {
        if (!file.isEmpty()) {
            try {
                PdfExtractionService.extractTextFromPdf(file);
                redirectAttributes.addFlashAttribute("message", "PDF uploaded and data extracted successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("message", "Failed to upload PDF: " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload.");
        }
        return "redirect:/uploadSuccess";
    }

    @GetMapping("/uploadSuccess")
    public String uploadSuccess() {
        return "uploadSuccess";
    }

    @GetMapping("/uploadFail")
    public String uploadFail() {
        return "uploadFail";
    }
}
