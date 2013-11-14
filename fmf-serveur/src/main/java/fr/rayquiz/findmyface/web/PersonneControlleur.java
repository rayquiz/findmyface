package fr.rayquiz.findmyface.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.rayquiz.findmyface.bo.Indice;
import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.dao.IPersonneDao;

@Controller
@RequestMapping("/personne")
public class PersonneControlleur {

    private IPersonneDao personneDao;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Personne recupListeServeurs(@PathVariable final long id) throws Exception {
        Personne p = new Personne();
        p.setNom("Mon prénom");
        p.setPrenom("Mon nom");
        p.getIndiceListe().add(new Indice(1, "enonce 1"));
        p.getIndiceListe().add(new Indice(2, "enonce 2"));

        // return personneDao.getById(id);
        return p;
    }

    public void setPersonneDao(final IPersonneDao personneDao) {
        this.personneDao = personneDao;
    }
}
