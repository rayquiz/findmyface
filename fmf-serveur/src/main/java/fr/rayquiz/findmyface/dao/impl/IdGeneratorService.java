package fr.rayquiz.findmyface.dao.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.dao.IIdGeneratorService;
import fr.rayquiz.findmyface.utils.ShardedCounter;
import static com.google.common.base.Preconditions.checkNotNull;

public class IdGeneratorService implements IIdGeneratorService {
    private Logger log = LoggerFactory.getLogger(getClass());
    private static final String PREFIX = "IdGenerator_";

    private static final Map<Difficulte, ShardedCounter> shardedMap = Maps.newEnumMap(Difficulte.class);
    static {
        for (int i = 0; i < Difficulte.values().length; i++) {
            Difficulte element = Difficulte.values()[i];
            shardedMap.put(element, new ShardedCounter(PREFIX + element.name()));
        }
    }

    @Override
    public long getCount(final Difficulte difficulte) {
        return shardedMap.get(checkNotNull(difficulte)).getCount();
    }

    @Override
    public long generateNewEntityId(final Difficulte difficulte) {
        ShardedCounter compteur = shardedMap.get(checkNotNull(difficulte));
        long newId;

        synchronized (compteur) {
            compteur.increment();
            newId = compteur.getCount();
        }

        long retour = buildEntityIdFromInfos(difficulte, newId);
        log.debug("Generation de l'id: {}", retour);
        return retour;
    }

    @Override
    public long buildEntityIdFromInfos(final Difficulte difficulte, final long id) {
        checkNotNull(difficulte);
        return Long.valueOf(difficulte.getImportance() + String.valueOf(id));
    }

}
