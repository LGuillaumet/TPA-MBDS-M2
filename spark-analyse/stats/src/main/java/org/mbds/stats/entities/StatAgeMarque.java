package org.mbds.stats.entities;

import java.io.Serializable;

public class StatAgeMarque  implements Serializable {

    private String marque;
    private long age;

    private double q0taux;
    private double q1taux;
    private double q2taux;
    private double q3taux;
    private double q4taux;

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

    public long getAge() {
        return age;
    }

    public void setAge(long age) {
        this.age = age;
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
