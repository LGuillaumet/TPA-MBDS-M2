package org.mbds.share.dto;

import java.io.Serializable;

public class MarketingDto  implements Serializable {

    private String id;
    private Long age;
    private String sexe;
    private Long taux;
    private String situation;
    private Long nbchildren;
    private Boolean havesecondcar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Long getTaux() {
        return taux;
    }

    public void setTaux(Long taux) {
        this.taux = taux;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public Long getNbchildren() {
        return nbchildren;
    }

    public void setNbchildren(Long nbchildren) {
        this.nbchildren = nbchildren;
    }

    public Boolean getHavesecondcar() {
        return havesecondcar;
    }

    public void setHavesecondcar(Boolean havesecondcar) {
        this.havesecondcar = havesecondcar;
    }

}
