package org.mbds.co2.dto;

import java.io.Serializable;

public class ValueDto implements Serializable {
    private Double bonusmalus;
    private Double rejet;
    private Double coutenergie;

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
}
