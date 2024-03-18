package com.interoperabilite.demo.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.interoperabilite.demo.Model.ArtistInfo;

@Repository
public interface ArtistInfoRepository extends CrudRepository<ArtistInfo, Long> {

}