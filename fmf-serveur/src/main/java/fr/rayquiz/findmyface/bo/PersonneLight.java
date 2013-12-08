package fr.rayquiz.findmyface.bo;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;

public class PersonneLight {

    private long id;

    private String nom;
    private String prenom;

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", id).add("nom", nom).add("prenom", prenom).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, nom, prenom);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        PersonneLight o = (PersonneLight) obj;
        return new EqualsBuilder().append(id, o.id).append(nom, o.nom).append(prenom, o.prenom).isEquals();
    }

    public void setId(final long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public PersonneLight withId(final long id) {
        setId(id);
        return this;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(final String nom) {
        this.nom = nom;
    }

    public PersonneLight withNom(final String nom) {
        setNom(nom);
        return this;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(final String prenom) {
        this.prenom = prenom;
    }

    public PersonneLight withPrenom(final String prenom) {
        setPrenom(prenom);
        return this;
    }

}
