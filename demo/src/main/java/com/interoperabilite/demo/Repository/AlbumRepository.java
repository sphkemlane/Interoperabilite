package com.interoperabilite.demo.Repository;


import com.interoperabilite.demo.Model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Ici, vous pouvez ajouter des méthodes personnalisées si nécessaire
}
