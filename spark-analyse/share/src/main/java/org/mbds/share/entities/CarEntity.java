package org.mbds.share.entities;

import org.mbds.share.dto.CarDto;

import java.io.Serializable;
import java.util.Objects;

public class CarEntity implements Serializable {

    private long id;
    private String marque;
    private String originalmarque;
    private String nom;
    private Integer puissance;
    private String longueur;
    private Integer nbplaces;
    private Integer nbportes;
    private String couleur;

    public CarEntity(){

    }

    public CarEntity(CarDto dto){
        marque = dto.getMarque();
        nom = dto.getNom();
        puissance = Math.toIntExact(dto.getPuissance());
        longueur = dto.getLongueur();
        nbplaces = Math.toIntExact(dto.getNbplaces());
        nbportes = Math.toIntExact(dto.getNbportes());
        couleur = dto.getCouleur();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOriginalmarque() {
        return originalmarque;
    }

    public void setOriginalmarque(String marque) {
        this.originalmarque = marque;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getPuissance() {
        return puissance;
    }

    public void setPuissance(Integer puissance) {
        this.puissance = puissance;
    }

    public String getLongueur() {
        return longueur;
    }

    public void setLongueur(String longueur) {
        this.longueur = longueur;
    }

    public Integer getNbplaces() {
        return nbplaces;
    }

    public void setNbplaces(Integer nbplaces) {
        this.nbplaces = nbplaces;
    }

    public Integer getNbportes() {
        return nbportes;
    }

    public void setNbportes(Integer nbportes) {
        this.nbportes = nbportes;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarEntity carEntity = (CarEntity) o;
        return Objects.equals(marque, carEntity.marque) && Objects.equals(nom, carEntity.nom) && Objects.equals(puissance, carEntity.puissance) && Objects.equals(longueur, carEntity.longueur) && Objects.equals(nbplaces, carEntity.nbplaces) && Objects.equals(nbportes, carEntity.nbportes) && Objects.equals(couleur, carEntity.couleur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marque, nom, puissance, longueur, nbplaces, nbportes, couleur);
    }

    @Override
    public String toString() {
        return "CarEntity{" +
                "id=" + id +
                ", marque='" + marque + '\'' +
                ", nom='" + nom + '\'' +
                ", puissance=" + puissance +
                ", longueur='" + longueur + '\'' +
                ", nbplaces=" + nbplaces +
                ", nbportes=" + nbportes +
                ", couleur='" + couleur + '\'' +
                '}';
    }
}
