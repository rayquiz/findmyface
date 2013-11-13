package fr.rayquiz.findmyface.dao;

import com.googlecode.objectify.NotFoundException;

import fr.rayquiz.findmyface.bo.Personne;

public interface IPersonneDao {

    long saveImmediate(Personne personne);

    void saveAsynchronously(Personne personne);

    Personne getById(final long id) throws NotFoundException;

}
