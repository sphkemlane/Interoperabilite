package com.interoperabilite.demo.Repository;

import com.interoperabilite.demo.Model.Album;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByTitleAndArtist(String title, String artist);

}