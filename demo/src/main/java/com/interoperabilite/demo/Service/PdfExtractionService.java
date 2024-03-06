package com.interoperabilite.demo.Service;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.interoperabilite.demo.Model.Album;
import com.interoperabilite.demo.Repository.AlbumRepository;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

@Service
public class PdfExtractionService {

    private final AlbumRepository albumRepository;

    public PdfExtractionService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public void extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            // Split the text into lines
            String[] lines = text.split("\n");

            // Temporary variables to store album information
            String currentArtist = null;
            String currentTitle = null;
            int currentYear = -1;

            // Process each line
            for (String line : lines) {
                if (line.startsWith("• Artiste :")) {
                    currentArtist = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("Albums :")) {
                    // Nothing to do here for now
                } else if (line.startsWith("\"")) {
                    // This line contains the album title and year
                    String[] parts = line.split("\"");
                    currentTitle = parts[1].trim(); // The album title is the second part

                    // Extract the year assuming it follows the title in parentheses
                    String yearPart = parts[2].trim(); // The year is the third part after the title
                    currentYear = Integer.parseInt(yearPart.substring(1, yearPart.length() - 1));

                    // Create a new Album entity and save it
                    Album album = new Album();
                    album.setTitle(currentTitle);
                    album.setArtist(currentArtist);
                    album.setReleaseYear(currentYear);
                    albumRepository.save(album);
                }
            }

            // Image extraction part
            PDPage page = document.getPage(0); // Example for the first page
            PDResources pdResources = page.getResources();
            for (COSName c : pdResources.getXObjectNames()) {
                PDXObject o = pdResources.getXObject(c);
                if (o instanceof PDImageXObject) {
                    PDImageXObject image = (PDImageXObject) o;
                    BufferedImage bimage = image.getImage();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bimage, "jpg", baos);
                    byte[] imageInByte = baos.toByteArray();

                    // Logic to associate the image with a specific album
                    // For example, if you have already extracted album information in the loop
                    // above
                    // You can now add the image to that album and save it
                    Album album = new Album(); // Or retrieve an existing album
                    album.setTitle(currentTitle);
                    album.setArtist(currentArtist);
                    album.setReleaseYear(currentYear);
                    album.setImage(imageInByte); // Add the extracted image
                    albumRepository.save(album);

                    break; // Example for processing only one image, exit the loop after processing the
                           // first image
                }
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}
