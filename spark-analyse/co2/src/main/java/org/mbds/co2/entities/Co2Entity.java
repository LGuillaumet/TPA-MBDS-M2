package org.mbds.co2.entities;

import java.io.Serializable;

public class Co2Entity implements Serializable {

    private String marque;
    private Double bonusmalus;
    private Double rejet;
    private Double coutenergie;

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Double getBonusmalus() {
        return bonusmalus;
    }

    public void setBonusmalus(Double bonusmalus) {
        this.bonusmalus = bonusmalus;
    }

    public Double getRejet() {
        return rejet;
    }

    public void setRejet(Double rejet) {
        this.rejet = rejet;
    }

    public Double getCoutenergie() {
        return coutenergie;
    }

    public void setCoutenergie(Double coutenergie) {
        this.coutenergie = coutenergie;
    }

    @Override
    public String toString() {
        return "Co2Entity{" +
                ", marque=" + marque +
                ", bonus=" + bonusmalus +
                ", emissionCo2='" + rejet + '\'' +
                ", coutEnergie=" + coutenergie +
                '}';
    }
}
