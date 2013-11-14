package fr.rayquiz.findmyface.bo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Unindex;

import fr.rayquiz.findmyface.utils.Phonetic;

@Entity
@Cache
@Unindex
public class Personne {
    @Id
    private Long id;

    private Genre genre;
    private String nom;
    private String prenom;

    @Index
    private String nomPhonetic;
    @Index
    private String prenomPhonetic;
    private int anneeNaissance;
    private int anneeDeces;

    private List<Indice> indiceListe;

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", id).add("genre", genre).add("nom", nom).add("prenom", prenom)
                .add("anneeNaissance", anneeNaissance).add("anneeDeces", anneeDeces)
                .add("indiceListe", StringUtils.join(getIndiceListe(), ",")).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, genre, nom, prenom, anneeNaissance, anneeDeces);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Personne o = (Personne) obj;
        return new EqualsBuilder().append(id, o.id).append(genre, o.genre).append(nom, o.nom).append(prenom, o.prenom)
                .append(anneeDeces, o.anneeDeces).append(anneeNaissance, o.anneeNaissance)
                .append(indiceListe, o.indiceListe).isEquals();
    }

    public Long getId() {
        return id;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(final Genre genre) {
        this.genre = genre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(final String nom) {
        this.nomPhonetic = Phonetic.genererPhonetic(checkNotNull(nom));
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(final String prenom) {
        this.prenomPhonetic = Phonetic.genererPhonetic(checkNotNull(prenom));
        this.prenom = prenom;
    }

    public String getNomPhonetic() {
        return nomPhonetic;
    }

    public String getPrenomPhonetic() {
        return prenomPhonetic;
    }

    public int getAnneeNaissance() {
        return anneeNaissance;
    }

    public void setAnneeNaissance(final int anneeNaissance) {
        this.anneeNaissance = anneeNaissance;
    }

    public int getAnneeDeces() {
        return anneeDeces;
    }

    public void setAnneeDeces(final int anneeDeces) {
        this.anneeDeces = anneeDeces;
    }

    public List<Indice> getIndiceListe() {
        if (indiceListe == null) indiceListe = Lists.newArrayListWithCapacity(2);
        return indiceListe;
    }

}
