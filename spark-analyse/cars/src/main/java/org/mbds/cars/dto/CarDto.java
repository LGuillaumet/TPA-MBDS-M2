package org.mbds.cars.dto;

import java.io.Serializable;
import java.util.Objects;

public class CarDto  implements Serializable {

    private String marque;
    private String nom;
    private long puissance;
    private String longueur;
    private long nbplaces;
    private long nbportes;
    private String couleur;

    public CarDto(){

    }

    public CarDto(RegistrationDto dto){
        marque = dto.getMarque();
        nom = dto.getNom();
        puissance = dto.getPuissance();
        longueur = dto.getLongueur();
        nbplaces = dto.getNbplaces();
        nbportes = dto.getNbportes();
        couleur = dto.getCouleur();
    }

    public CarDto(CatalogueDto dto){
        marque = dto.getMarque();
        nom = dto.getNom();
        puissance = dto.getPuissance();
        longueur = dto.getLongueur();
        nbplaces = dto.getNbplaces();
        nbportes = dto.getNbportes();
        couleur = dto.getCouleur();
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

    public Long getPuissance() {
        return puissance;
    }

    public void setPuissance(Long puissance) {
        this.puissance = puissance;
    }

    public String getLongueur() {
        return longueur;
    }

    public void setLongueur(String longueur) {
        this.longueur = longueur;
    }

    public Long getNbplaces() {
        return nbplaces;
    }

    public void setNbplaces(Long nbplaces) {
        this.nbplaces = nbplaces;
    }

    public Long getNbportes() {
        return nbportes;
    }

    public void setNbportes(Long nbportes) {
        this.nbportes = nbportes;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public int hashCode() {
        return Objects.hash(marque, nom, puissance, longueur, nbplaces, nbportes, couleur);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(obj == null) return false;
        return obj instanceof CarDto && this.equals((CarDto) obj);
    }

    public boolean equals(CarDto dto){
        return Objects.equals(marque, dto.getMarque()) &&
                Objects.equals(nom, dto.getNom()) &&
                puissance == dto.getPuissance() &&
                Objects.equals(longueur, dto.getLongueur()) &&
                nbplaces == dto.getNbplaces()  &&
                nbportes == dto.getNbportes() &&
                Objects.equals(couleur, dto.getCouleur());
    }

    @Override
    public String toString() {
        return "CarDto{" +
                " marque='" + marque + '\'' +
                ", nom='" + nom + '\'' +
                ", puissance=" + puissance +
                ", longueur='" + longueur + '\'' +
                ", nbplaces=" + nbplaces +
                ", nbportes=" + nbportes +
                '}';
    }
}
