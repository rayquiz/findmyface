package fr.rayquiz.findmyface.dao.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;

import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.dao.IPersonneDao;

public class PersonneDao implements IPersonneDao {
    static {
        ObjectifyService.register(Personne.class);
    }

    @Override
    public void saveAsynchronously(final Personne personne) {
        checkNotNull(personne, "Impossible de sauver une personne null");
        ofy().save().entity(personne);
    }

    @Override
    public long saveImmediate(final Personne personne) {
        checkNotNull(personne, "Impossible de sauver une personne null");
        return ofy().save().entity(personne).now().getId();
    }

    @Override
    public Personne getById(final long id) throws NotFoundException {
        return ofy().load().key(Key.create(Personne.class, id)).safe();
    }

}
