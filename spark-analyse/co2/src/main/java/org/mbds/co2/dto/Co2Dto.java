package org.mbds.co2.dto;

public class Co2Dto {
    private long id;
    private String marquemodel;
    private String bonusmalus;
    private String rejection;
    private String energiecost;

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

    public String getBonusmalus() {
        return bonusmalus;
    }

    public void setBonusmalus(String bonusmalus) {
        this.bonusmalus = bonusmalus;
    }

    public String getRejection() {
        return rejection;
    }

    public void setRejection(String rejection) {
        this.rejection = rejection;
    }

    public String getEnergiecost() {
        return energiecost;
    }

    public void setEnergiecost(String energiecost) {
        this.energiecost = energiecost;
    }
}
