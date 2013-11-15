package fr.rayquiz.findmyface.dao.bo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import fr.rayquiz.findmyface.bo.Difficulte;

@Entity
@Cache
public class RandomBo {
    @Parent
    public final Key parent;

    @Id
    private String id;

    @Index
    private Difficulte difficulte;
    private List<Long> idListe;

    public RandomBo() {
        super();
        parent = KeyFactory.createKey("RandomBoHandler", "randombo");
    }

    public RandomBo(final Difficulte difficulte) {
        this();
        setDifficulte(difficulte);
    }

    public Key getParent() {
        return parent;
    }

    public void setDifficulte(final Difficulte difficulte) {
        this.difficulte = checkNotNull(difficulte);
        this.id = this.difficulte.name();
    }

    public Difficulte getDifficulte() {
        return difficulte;
    }

    public List<Long> getIdListe() {
        if (idListe == null) idListe = Lists.newLinkedList();
        return idListe;
    }
}
