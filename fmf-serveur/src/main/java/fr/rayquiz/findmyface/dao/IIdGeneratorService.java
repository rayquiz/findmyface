package fr.rayquiz.findmyface.dao;

import fr.rayquiz.findmyface.bo.Difficulte;

public interface IIdGeneratorService {

    long getCount(Difficulte difficulte);

    long generateNewId(Difficulte difficulte);

}
