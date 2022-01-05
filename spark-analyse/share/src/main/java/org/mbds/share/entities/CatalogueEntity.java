package org.mbds.share.entities;

import java.io.Serializable;

public class CatalogueEntity implements Serializable {

    private long id;
    private Boolean occasion;
    private double prix;
    private long idCar;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public long getIdCar() {
        return idCar;
    }

    public void setIdCar(long idCar) {
        this.idCar = idCar;
    }

    @Override
    public String toString() {
        return "CatalogueEntity{" +
                "id=" + id +
                ", occasion=" + occasion +
                ", prix=" + prix +
                ", idCar=" + idCar +
                '}';
    }
}
