package com.interoperabilite.demo.Repository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.interoperabilite.demo.Model.ArtistInfo;

@Repository
public interface ArtistInfoRepository extends CrudRepository<ArtistInfo, Long> {
    // Vous n'avez pas besoin d'ajouter d'autres méthodes ici pour cet exemple,
    // save() est déjà défini par CrudRepository.
}