package fr.rayquiz.findmyface.dao.bo;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Entity
@Cache
public class JoueurInfosBo {
    @Parent
    public final Key parent;

    @Id
    private String id;

    private List<String> idTrouveListe;

    public JoueurInfosBo(String id) {
        this();
        this.id = id;
    }

    public JoueurInfosBo() {
        super();
        parent = KeyFactory.createKey("RandomBoHandler", "randombo");
    }

    public Key getParent() {
        return parent;
    }

    public List<String> getIdTrouveListe() {
        if (idTrouveListe == null) idTrouveListe = Lists.newLinkedList();
        return idTrouveListe;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", id)
                .add("idTrouveListe", StringUtils.join(getIdTrouveListe(), ",")).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, idTrouveListe);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        JoueurInfosBo o = (JoueurInfosBo) obj;
        return new EqualsBuilder().append(id, o.id).append(parent, o.parent).append(idTrouveListe, o.idTrouveListe)
                .isEquals();
    }
}
