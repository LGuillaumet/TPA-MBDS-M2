package org.mbds.stats.entities;

import java.io.Serializable;

public class StatTauxMarque  implements Serializable {

    private String marque;
    private long taux;
    private double q0age;
    private double q1age;
    private double q2age;
    private double q3age;
    private double q4age;
    private double q0nbchildren;
    private double q1nbchildren;
    private double q2nbchildren;
    private double q3nbchildren;
    private double q4nbchildren;

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public long getTaux() {
        return taux;
    }

    public void setTaux(long taux) {
        this.taux = taux;
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

    public double getQ0nbchildren() {
        return q0nbchildren;
    }

    public void setQ0nbchildren(double q0nbchildren) {
        this.q0nbchildren = q0nbchildren;
    }

    public double getQ1nbchildren() {
        return q1nbchildren;
    }

    public void setQ1nbchildren(double q1nbchildren) {
        this.q1nbchildren = q1nbchildren;
    }

    public double getQ2nbchildren() {
        return q2nbchildren;
    }

    public void setQ2nbchildren(double q2nbchildren) {
        this.q2nbchildren = q2nbchildren;
    }

    public double getQ3nbchildren() {
        return q3nbchildren;
    }

    public void setQ3nbchildren(double q3nbchildren) {
        this.q3nbchildren = q3nbchildren;
    }

    public double getQ4nbchildren() {
        return q4nbchildren;
    }

    public void setQ4nbchildren(double q4nbchildren) {
        this.q4nbchildren = q4nbchildren;
    }
}
