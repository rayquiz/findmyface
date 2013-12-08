package fr.rayquiz.findmyface.bo.utils;

import com.google.common.base.Function;

import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.bo.PersonneLight;
import static com.google.common.base.Preconditions.checkNotNull;

public class PersonneToPersonneLightTransformer implements Function<Personne, PersonneLight> {

    @Override
    public PersonneLight apply(Personne input) {
        checkNotNull(input);
        new PersonneLight().withNom(input.getNom()).withPrenom(input.getPrenom()).withId(input.getId());
        return null;
    }

}
