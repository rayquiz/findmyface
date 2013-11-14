package fr.rayquiz.findmyface.bo;

import org.apache.commons.lang.builder.EqualsBuilder;

import com.google.common.base.Objects;
import com.googlecode.objectify.annotation.Embed;

@Embed
public class Indice {

    private int numero;
    private String enonce;
    private String urlImage;

    public Indice() {
        super();
    }

    public Indice(final int numero, final String enonce) {
        super();
        this.numero = numero;
        this.enonce = enonce;
    }

    public Indice(final int numero, final String enonce, final String urlImage) {
        super();
        this.numero = numero;
        this.enonce = enonce;
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("numero", numero).add("enonce", enonce).add("urlImage", urlImage)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numero, enonce, urlImage);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Indice o = (Indice) obj;

        return new EqualsBuilder().append(numero, o.numero).append(enonce, o.enonce).append(urlImage, o.urlImage)
                .isEquals();
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(final int numero) {
        this.numero = numero;
    }

    public String getEnonce() {
        return enonce;
    }

    public void setEnonce(final String enonce) {
        this.enonce = enonce;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(final String urlImage) {
        this.urlImage = urlImage;
    }

}
