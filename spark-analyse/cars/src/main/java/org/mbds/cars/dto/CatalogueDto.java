package org.mbds.cars.dto;

import java.io.Serializable;

public class CatalogueDto  implements Serializable {

    private String marque;
    private String nom;
    private Long puissance;
    private String longueur;
    private Long nbplaces;
    private Long nbportes;
    private Boolean occasion;
    private Double prix;
    private String couleur;


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

    public Boolean getOccasion() {
        return occasion;
    }

    public void setOccasion(Boolean occasion) {
        this.occasion = occasion;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public String getCouleur() {
        return couleur;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    @Override
    public String toString() {
        return "CatalogueDto{" +
                "marque='" + marque + '\'' +
                ", nom='" + nom + '\'' +
                ", puissance=" + puissance +
                ", longueur='" + longueur + '\'' +
                ", nbplaces=" + nbplaces +
                ", nbportes=" + nbportes +
                ", occasion=" + occasion +
                ", prix=" + prix +
                ", couleur='" + couleur + '\'' +
                '}';
    }
}
