package fr.rayquiz.findmyface.dao;

import fr.rayquiz.findmyface.dao.bo.JoueurInfosBo;
import fr.rayquiz.findmyface.exceptions.NotLoggedInException;

public interface IJoueurInfosDao {

    JoueurInfosBo getCurrentJoueurInfos() throws NotLoggedInException;

    void saveAsynchrone(JoueurInfosBo joueur);

}
