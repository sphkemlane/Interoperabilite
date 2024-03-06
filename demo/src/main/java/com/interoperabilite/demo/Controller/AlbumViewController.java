package com.interoperabilite.demo.Controller;

import com.interoperabilite.demo.Model.Album;
import com.interoperabilite.demo.Repository.AlbumRepository;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AlbumViewController {

    private final AlbumRepository albumRepository;

    public AlbumViewController(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    @GetMapping("/albums")
    public String showAlbumsPage(Model model) {
        List<Album> albums = albumRepository.findAll();
        model.addAttribute("albums", albums);
        return "albums"; // Assurez-vous que le fichier albums.html est dans src/main/resources/templates
    }
}
