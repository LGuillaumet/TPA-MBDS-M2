package org.mbds.stats.entities;

import java.io.Serializable;

public class StatNbchildrenMarque  implements Serializable {

    private String marque;
    private long nbchildren;
    private double q0age;
    private double q1age;
    private double q2age;
    private double q3age;
    private double q4age;
    private double q0taux;
    private double q1taux;
    private double q2taux;
    private double q3taux;
    private double q4taux;

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public long getNbchildren() {
        return nbchildren;
    }

    public void setNbchildren(long nbchildren) {
        this.nbchildren = nbchildren;
    }

    public double getQ0age() {
        return q0age;
    }

    public void setQ0age(double q0age) {
        this.q0age = q0age;
    }

    public double getQ1age() {
        return q1age;
    }

    public void setQ1age(double q1age) {
        this.q1age = q1age;
    }

    public double getQ2age() {
        return q2age;
    }

    public void setQ2age(double q2age) {
        this.q2age = q2age;
    }

    public double getQ3age() {
        return q3age;
    }

    public void setQ3age(double q3age) {
        this.q3age = q3age;
    }

    public double getQ4age() {
        return q4age;
    }

    public void setQ4age(double q4age) {
        this.q4age = q4age;
    }

    public double getQ0taux() {
        return q0taux;
    }

    public void setQ0taux(double q0taux) {
        this.q0taux = q0taux;
    }

    public double getQ1taux() {
        return q1taux;
    }

    public void setQ1taux(double q1taux) {
        this.q1taux = q1taux;
    }

    public double getQ2taux() {
        return q2taux;
    }

    public void setQ2taux(double q2taux) {
        this.q2taux = q2taux;
    }

    public double getQ3taux() {
        return q3taux;
    }

    public void setQ3taux(double q3taux) {
        this.q3taux = q3taux;
    }

    public double getQ4taux() {
        return q4taux;
    }

    public void setQ4taux(double q4taux) {
        this.q4taux = q4taux;
    }
}
