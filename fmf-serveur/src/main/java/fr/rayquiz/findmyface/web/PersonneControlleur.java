package fr.rayquiz.findmyface.web;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.objectify.NotFoundException;

import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.dao.IPersonneDao;

@Controller
@RequestMapping("/personne")
public class PersonneControlleur {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private IPersonneDao personneDao;

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
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Set<Personne> getByNomOuPrenom(@PathVariable final String texte) {
        return personneDao.getByNomOuPrenomPhonetic(texte);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public long save(@RequestBody final Personne personne) throws Exception {
        checkNotNull(personne, "Objet fourni null !");
        return personneDao.saveImmediate(personne);
    }

    public void setPersonneDao(final IPersonneDao personneDao) {
        this.personneDao = personneDao;
    }
}
