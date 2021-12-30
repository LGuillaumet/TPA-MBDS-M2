package org.mbds.clients.entities;

import java.io.Serializable;

public class ClientEntity implements Serializable {

    private long id;
    private Integer age;
    private String sexe;
    private Integer taux;
    private String situation;
    private Integer nbchildren;
    private boolean havesecondcar;
    private String registrationid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public boolean isHavesecondcar() {
        return havesecondcar;
    }

    public void setHavesecondcar(boolean havesecondcar) {
        this.havesecondcar = havesecondcar;
    }

    public String getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(String registrationid) {
        this.registrationid = registrationid;
    }

    @Override
    public String toString() {
        return "ClientEntity{" +
                "id=" + id +
                ", age=" + age +
                ", sexe='" + sexe + '\'' +
                ", taux=" + taux +
                ", situation='" + situation + '\'' +
                ", nbchildren=" + nbchildren +
                ", havesecondcar=" + havesecondcar +
                ", registrationid='" + registrationid + '\'' +
                '}';
    }
}
