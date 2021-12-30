package org.mbds.cars.dto;

import java.io.Serializable;

public class CatalogueDto  implements Serializable {

    private String marque;
    private String nom;
    private long puissance;
    private String longueur;
    private long nbplaces;
    private long nbportes;
    private Boolean occasion;
    private double prix;
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

    public long getPuissance() {
        return puissance;
    }

    public void setPuissance(long puissance) {
        this.puissance = puissance;
    }

    public String getLongueur() {
        return longueur;
    }

    public void setLongueur(String longueur) {
        this.longueur = longueur;
    }

    public long getNbplaces() {
        return nbplaces;
    }

    public void setNbplaces(long nbplaces) {
        this.nbplaces = nbplaces;
    }

    public long getNbportes() {
        return nbportes;
    }

    public void setNbportes(long nbportes) {
        this.nbportes = nbportes;
    }

    public Boolean getOccasion() {
        return occasion;
    }

    public void setOccasion(Boolean occasion) {
        this.occasion = occasion;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
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
