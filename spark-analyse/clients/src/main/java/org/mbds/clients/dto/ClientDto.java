package org.mbds.clients.dto;

public class ClientDto {

    private long id;
    private long age;
    private String sexe;
    private long taux;
    private String situation;
    private long nbchildren;
    private boolean havesecondcar;
    private String registrationid;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public long getTaux() {
        return taux;
    }

    public void setTaux(long taux) {
        this.taux = taux;
    }

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }

    public long getNbchildren() {
        return nbchildren;
    }

    public void setNbchildren(long nbchildren) {
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
}
