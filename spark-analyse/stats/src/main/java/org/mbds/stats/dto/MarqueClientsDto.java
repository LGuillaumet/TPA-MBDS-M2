package org.mbds.stats.dto;

import java.io.Serializable;

public class MarqueClientsDto implements Serializable {

    private String marque;
    private Long age;
    private Long taux;
    private Long nbchildren;

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getTaux() {
        return taux;
    }

    public void setTaux(Long taux) {
        this.taux = taux;
    }

    public Long getNbchildren() {
        return nbchildren;
    }

    public void setNbchildren(Long nbchildren) {
        this.nbchildren = nbchildren;
    }

}
