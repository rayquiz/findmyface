package fr.rayquiz.findmyface.web;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Collections2;
import com.googlecode.objectify.NotFoundException;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.bo.PersonneLight;
import fr.rayquiz.findmyface.bo.utils.PersonneToPersonneLightTransformer;
import fr.rayquiz.findmyface.dao.IJoueurInfosDao;
import fr.rayquiz.findmyface.dao.IPersonneDao;
import fr.rayquiz.findmyface.dao.bo.JoueurInfosBo;
import static com.google.common.base.Preconditions.checkNotNull;

@Controller
@RequestMapping(RoutageConstantes.PERSONNE)
public class PersonneControlleur {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private IPersonneDao personneDao;
    private IJoueurInfosDao joueurInfosDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Personne getById(@PathVariable final long id) {
        try {
            return personneDao.getById(id);
        } catch (NotFoundException e) {
            log.warn("Demande d'une personne inexistante (id:{})", id, e);
            return null;
        }
    }

    @RequestMapping(
            value = "/recherche/{texte}",
            params = "light=true",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<PersonneLight> getByNomOuPrenomLight(@PathVariable final String texte) {
        Collection<Personne> set = getByNomOuPrenom(texte);
        return Collections2.transform(set, new PersonneToPersonneLightTransformer());
    }

    @RequestMapping(
            value = "/recherche/{texte}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Collection<Personne> getByNomOuPrenom(@PathVariable final String texte) {
        return personneDao.getByNomOuPrenomPhonetic(texte);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long save(@RequestBody final Personne personne) throws Exception {
        checkNotNull(personne, "Objet fourni null !");
        return personneDao.saveImmediate(personne);
    }

    @RequestMapping(
            value = "/random/{difficulte}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Personne getRandomWithDifficulte(@PathVariable final Difficulte difficulte) {
        JoueurInfosBo joueur = joueurInfosDao.getCurrentJoueurInfos();
        checkNotNull(joueur, "Les informations du joueur sont obligatoires");

        Personne personne = personneDao.getRandomByDifficulte(difficulte, joueur);
        checkNotNull(personne, "La personne est obligatoire");

        log.debug("Ajout de la personne {} à la liste du joueur {}", personne.getId(), joueur.getId());
        joueur.getIdTrouveListeByDifficulte(personne.getDifficulte()).add(personne.getId());
        joueurInfosDao.saveAsynchrone(joueur);

        return personne;
    }

    @RequestMapping(value = "/random", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Personne getRandomNoContrainte() {
        return getRandomWithDifficulte(null);
    }

    public void setPersonneDao(final IPersonneDao personneDao) {
        this.personneDao = personneDao;
    }

    public void setJoueurInfosDao(IJoueurInfosDao joueurInfosDao) {
        this.joueurInfosDao = joueurInfosDao;
    }
}
