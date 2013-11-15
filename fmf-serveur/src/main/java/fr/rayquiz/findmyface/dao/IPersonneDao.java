package fr.rayquiz.findmyface.dao;

import java.util.Set;

import javax.annotation.Nullable;

import com.googlecode.objectify.NotFoundException;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.bo.Personne;

/**
 * Interface du service de requêtage des objets {@link Personne}
 * 
 * @author PJ
 * 
 */
public interface IPersonneDao {

    long saveImmediate(Personne personne);

    void saveAsynchronously(Personne personne);

    Personne getById(final long id) throws NotFoundException;

    Set<Personne> getByNomOuPrenomPhonetic(String nomPrenom);

    Personne getRandomByDifficulte(@Nullable Difficulte difficulte);

}
