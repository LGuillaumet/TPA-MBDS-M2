package org.mbds.clients.entities;

import java.io.Serializable;

public class MarketingEntity  implements Serializable {

    private String id;
    private Integer age;
    private String sexe;
    private Integer taux;
    private String situation;
    private Integer nbchildren;
    private Boolean havesecondcar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public Integer getTaux() {
        return taux;
    }

    public void setTaux(Integer taux) {
        this.taux = taux;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public Integer getNbchildren() {
        return nbchildren;
    }

    public void setNbchildren(Integer nbchildren) {
        this.nbchildren = nbchildren;
    }

    public Boolean getHavesecondcar() {
        return havesecondcar;
    }

    public void setHavesecondcar(Boolean havesecondcar) {
        this.havesecondcar = havesecondcar;
    }
}
