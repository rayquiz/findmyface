package fr.rayquiz.findmyface.dao.bo;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Serialize;

import fr.rayquiz.findmyface.bo.Difficulte;
import static com.google.common.base.Preconditions.checkNotNull;

@Entity
@Cache
public class JoueurInfosBo {
    @Parent
    public final Key parent;

    @Id
    private String id;

    @Serialize
    private Map<Difficulte, Set<Long>> difficulteMap;

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

    public Map<Difficulte, Set<Long>> getDifficulteMap() {
        if (difficulteMap == null) {
            difficulteMap = Maps.newEnumMap(Difficulte.class);
        }
        return difficulteMap;
    }

    public Set<Long> getIdTrouveListeByDifficulte(Difficulte difficulte) {
        checkNotNull(difficulte);
        Set<Long> liste = getDifficulteMap().get(difficulte);
        if (liste == null) {
            liste = Sets.newHashSet();
            getDifficulteMap().put(difficulte, liste);
        }
        return liste;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("id", id)
                .add("difficulteMap", Joiner.on(";").withKeyValueSeparator(":").join(getDifficulteMap())).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, difficulteMap);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        JoueurInfosBo o = (JoueurInfosBo) obj;
        return new EqualsBuilder().append(id, o.id).append(parent, o.parent).append(difficulteMap, o.difficulteMap)
                .isEquals();
    }
}
