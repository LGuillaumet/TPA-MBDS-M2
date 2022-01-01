package org.mbds.co2.entities;

import java.io.Serializable;

public class Co2Entity implements Serializable {
    private long id;
    private String marquemodel;
    private Integer bonusmalus;
    private Integer rejection;
    private Integer energiecost;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMarquemodel() {
        return marquemodel;
    }

    public void setMarquemodel(String marquemodel) {
        this.marquemodel = marquemodel;
    }

    public Integer getBonusmalus() {
        return bonusmalus;
    }

    public void setBonusmalus(Integer bonusmalus) {
        this.bonusmalus = bonusmalus;
    }

    public Integer getRejection() {
        return rejection;
    }

    public void setRejection(Integer rejection) {
        this.rejection = rejection;
    }

    public Integer getEnergiecost() {
        return energiecost;
    }

    public void setEnergiecost(Integer energiecost) {
        this.energiecost = energiecost;
    }

    @Override
    public String toString() {
        return "Co2Entity{" +
                "id=" + id +
                ", marque=" + marquemodel +
                ", bonus=" + bonusmalus +
                ", emissionCo2='" + rejection + '\'' +
                ", coutEnergie=" + energiecost +
                '}';
    }
}
