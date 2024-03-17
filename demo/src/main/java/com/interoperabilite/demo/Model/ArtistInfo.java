package com.interoperabilite.demo.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Artist")
public class ArtistInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "label")
    private String label;

    @Column(name = "artistic_genre")
    private String artisticGenre;

    @Column(name = "wikidata_id")
    private String wikidataId;

    @Column(name = "dbpedia_id")
    private String dbpediaId;

    // Standard getters and setters

    // Ajout d'un constructeur par défaut
    public ArtistInfo() {
    }

    // Constructeur avec paramètres
    public ArtistInfo(String birthDate, String realName, String nationality, String occupation, String label,
            String artisticGenre , String wikidataId, String dbpediaId ) {
        this.birthDate = birthDate;
        this.realName = realName;
        this.nationality = nationality;
        this.occupation = occupation;
        this.label = label;
        this.artisticGenre = artisticGenre;
        this.wikidataId = wikidataId;
        this.dbpediaId = dbpediaId;
    }

    // Reste de la classe (getters, setters, toString)
}
