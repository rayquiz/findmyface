package fr.rayquiz.findmyface.dao;

import fr.rayquiz.findmyface.bo.Difficulte;

public interface IIdGeneratorService {

    long getCount(Difficulte difficulte);

    long generateNewEntityId(Difficulte difficulte);

    long buildEntityIdFromInfos(Difficulte difficulte, long id);

}
