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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getNationality() {
        return this.nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOccupation() {
        return this.occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getArtisticGenre() {
        return this.artisticGenre;
    }

    public void setArtisticGenre(String artisticGenre) {
        this.artisticGenre = artisticGenre;
    }

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

    public String getWikidataId() {
        return wikidataId;
    }

    public void setWikidataId(String wikidataId) {
        this.wikidataId = wikidataId;
    }

    public String getDbpediaId() {
        return dbpediaId;
    }

    public void setDbpediaId(String dbpediaId) {
        this.dbpediaId = dbpediaId;
    }

    public ArtistInfo() {
    }

    // Constructeur avec paramètres
    public ArtistInfo(String birthDate, String realName, String nationality, String occupation, String label,
            String artisticGenre, String wikidataId, String dbpediaId) {
        this.birthDate = birthDate;
        this.realName = realName;
        this.nationality = nationality;
        this.occupation = occupation;
        this.label = label;
        this.artisticGenre = artisticGenre;
        this.wikidataId = wikidataId;
        this.dbpediaId = dbpediaId;
    }

}
