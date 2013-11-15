package fr.rayquiz.findmyface.dao.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.dao.IIdGeneratorService;
import fr.rayquiz.findmyface.dao.IPersonneDao;
import fr.rayquiz.findmyface.utils.ObjectifyUtil;
import fr.rayquiz.findmyface.utils.Phonetic;

public class PersonneDao implements IPersonneDao {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private IIdGeneratorService idGeneratorService;

    static {
        ObjectifyService.register(Personne.class);
    }

    public void setIdGeneratorService(final IIdGeneratorService idGeneratorService) {
        this.idGeneratorService = idGeneratorService;
    }

    @Override
    public void saveAsynchronously(final Personne personne) {
        checkNotNull(personne, "Impossible de sauver une personne null");
        checkNotNull(personne.getDifficulte(), "Impossible d'avoir une difficulte non renseignée");

        personne.setId(idGeneratorService.generateNewId(personne.getDifficulte()));

        ofy().save().entity(personne);
    }

    @Override
    public long saveImmediate(final Personne personne) {
        checkNotNull(personne, "Impossible de sauver une personne null");
        checkNotNull(personne.getDifficulte(), "Impossible d'avoir une difficulte non renseignée");

        personne.buildPhonetic();
        if (personne.getId() != 0) {
            log.warn("Tentative d'enregistrement d'une nouvelle personne avec Id déja renseigné (id={}), ecrasement",
                    personne.getId());
        }
        personne.setId(idGeneratorService.generateNewId(personne.getDifficulte()));

        return ofy().save().entity(personne).now().getId();
    }

    @Override
    public Personne getById(final long id) throws NotFoundException {
        return ofy().load().key(Key.create(Personne.class, id)).safe();
    }

    @Override
    public Set<Personne> getByNomOuPrenomPhonetic(final String nomPrenom) {
        checkArgument(StringUtils.isNotEmpty(nomPrenom), "Le nom ou prenom doit être renseigné");

        Set<Personne> retour = Sets.newHashSet();
        String phonex = Phonetic.genererPhonetic(nomPrenom);

        log.debug("Recherche de la personne '{}' (saisie: '{}')", phonex, nomPrenom);
        LoadType<Personne> query = ofy().load().type(Personne.class);

        Query<Personne> resultQuery = ObjectifyUtil.fieldStartsWith(query, "nomPhonetic", phonex);
        Iterables.addAll(retour, resultQuery);

        resultQuery = ObjectifyUtil.fieldStartsWith(query, "prenomPhonetic", phonex);
        Iterables.addAll(retour, resultQuery);

        log.debug("Retour de {} elements", retour.size());

        return retour;
    }

    @Override
    public Personne getRandomByDifficulte(final Difficulte difficulte) {
        ofy().load().type(Personne.class);
        return null;
    }
}
