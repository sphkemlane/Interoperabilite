package com.interoperabilite.demo.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.interoperabilite.demo.Model.ArtistInfo;

@Repository
public interface ArtistInfoRepository extends JpaRepository<ArtistInfo, Long> {
}
