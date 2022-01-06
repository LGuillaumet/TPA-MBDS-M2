package org.mbds.stats.models;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Objects;

public class MarqueKey implements Serializable, Comparable<MarqueKey> {

    private String marque;
    private Long value;

    public MarqueKey(){

    }

    public MarqueKey(String marque, Long value) {
        this.marque = marque;
        this.value = value;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MarqueKey{" +
                "marque='" + marque + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarqueKey marqueKey = (MarqueKey) o;
        return Objects.equals(marque, marqueKey.marque) && Objects.equals(value, marqueKey.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(marque, value);
    }

    @Override
    public int compareTo(@NotNull MarqueKey o) {
        int i = this.marque.compareTo(o.getMarque());
        if(i!=0)
            return i;
        else
            return Long.compare(this.value, o.value);
    }
}
