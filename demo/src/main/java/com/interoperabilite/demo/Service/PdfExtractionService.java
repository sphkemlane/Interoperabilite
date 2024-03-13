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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
            String[] lines = text.split("\n");
    
            String currentArtist = null;
            List<Album> albumsToSave = new ArrayList<>();
            int imageIndex = 0; // Keep track of the image index
    
            List<PDPage> pages = new ArrayList<>();
            document.getDocumentCatalog().getPages().forEach(pages::add);
    
            Iterator<PDPage> pageIterator = pages.iterator();
            PDPage currentPage = pageIterator.hasNext() ? pageIterator.next() : null;
            PDResources pdResources = currentPage != null ? currentPage.getResources() : null;
            Iterator<COSName> imageIterator = pdResources != null ? pdResources.getXObjectNames().iterator() : null;
    
            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("• Artiste :")) {
                    currentArtist = line.substring(line.indexOf(":") + 1).trim();
                } else if (line.startsWith("\"")) {
                    String[] parts = line.split("\"");
                    if (parts.length >= 3) {
                        String currentTitle = parts[1].trim();
                        String yearPart = parts[2].trim().replaceAll("[^\\d]", ""); // Remove non-digits
                        int currentYear = Integer.parseInt(yearPart);
    
                        Album album = new Album();
                        album.setTitle(currentTitle);
                        album.setArtist(currentArtist);
                        album.setReleaseYear(currentYear);
                        albumsToSave.add(album);
    
                        System.out.println("Processing: Artist=" + currentArtist + ", Title=" + currentTitle + ", Year=" + currentYear);
                    }
                } else if (line.startsWith("Photo :") && imageIterator != null) {
                    // Assign the image to the current album
                    if (imageIterator.hasNext() && imageIndex < albumsToSave.size()) {
                        COSName cosName = imageIterator.next();
                        PDXObject xObject = pdResources.getXObject(cosName);
                        if (xObject instanceof PDImageXObject) {
                            PDImageXObject image = (PDImageXObject) xObject;
                            BufferedImage bimage = image.getImage();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ImageIO.write(bimage, "jpg", baos);
                            byte[] imageInByte = baos.toByteArray();
    
                            Album album = albumsToSave.get(imageIndex++);
                            album.setImage(imageInByte);
                            System.out.println("Assigned image to album: " + album.getTitle());
                        }
                    }
                    if (!imageIterator.hasNext() && pageIterator.hasNext()) {
                        currentPage = pageIterator.next();
                        pdResources = currentPage.getResources();
                        imageIterator = pdResources.getXObjectNames().iterator();
                    }
                }
            }
    
            // Save all albums at once
            albumRepository.saveAll(albumsToSave);
    
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }
}    