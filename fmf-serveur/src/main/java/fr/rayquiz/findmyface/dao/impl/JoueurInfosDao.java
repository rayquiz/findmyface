package fr.rayquiz.findmyface.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.ObjectifyService;

import fr.rayquiz.findmyface.dao.IJoueurInfosDao;
import fr.rayquiz.findmyface.dao.bo.JoueurInfosBo;
import fr.rayquiz.findmyface.exceptions.NotLoggedInException;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

public class JoueurInfosDao implements IJoueurInfosDao {
    static {
        ObjectifyService.register(JoueurInfosBo.class);
    }
    private final Logger log = LoggerFactory.getLogger(getClass());

    private UserService userService = UserServiceFactory.getUserService();

    @Override
    public JoueurInfosBo getCurrentJoueurInfos() throws NotLoggedInException {

        if (!userService.isUserLoggedIn()) throw new NotLoggedInException();

        JoueurInfosBo loadEntity = new JoueurInfosBo(userService.getCurrentUser().getUserId());
        log.debug("Chargement de l'utilisateur {}", loadEntity.getId());

        JoueurInfosBo retour = ofy().load().entity(loadEntity).now();
        if (retour == null) {
            log.debug("Utilisateur inexistant en base, creation d'un nouveau");
            return loadEntity;
        } else {
            return retour;
        }
    }

    @Override
    public void save(JoueurInfosBo joueur) {
        ofy().save().entity(checkNotNull(joueur));
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
