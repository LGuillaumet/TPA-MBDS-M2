package org.mbds.clients.dto;

public class ClientDto {

    private Long id;
    private Long age;
    private String sexe;
    private Long taux;
    private String situation;
    private Long nbchildren;
    private Boolean havesecondcar;
    private String registrationid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getRegistrationid() {
        return registrationid;
    }

    public void setRegistrationid(String registrationid) {
        this.registrationid = registrationid;
    }
}
