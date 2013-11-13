package fr.rayquiz.findmyface.bo;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Embed;

@Embed
public class Indice {
    private String enonce;
    private String type;

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("enonce", enonce).add("type", type).toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(enonce, type);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Indice o = (Indice) obj;

        return new EqualsBuilder().append(enonce, o.enonce).append(type, o.type).isEquals();
    }

}
