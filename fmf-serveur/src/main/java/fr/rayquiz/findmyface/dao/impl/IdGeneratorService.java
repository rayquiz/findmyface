package fr.rayquiz.findmyface.dao.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.Map;

import com.google.common.collect.Maps;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.dao.IIdGeneratorService;
import fr.rayquiz.findmyface.dao.bo.RandomBo;
import fr.rayquiz.findmyface.utils.ShardedCounter;

public class IdGeneratorService implements IIdGeneratorService {
    private static final String PREFIX = "IdGenerator_";

    private static final Map<Difficulte, ShardedCounter> shardedMap = Maps.newEnumMap(Difficulte.class);
    static {
        for (int i = 0; i < Difficulte.values().length; i++) {
            Difficulte element = Difficulte.values()[i];
            shardedMap.put(element, new ShardedCounter(PREFIX + element.name()));
        }

        ObjectifyService.register(RandomBo.class);
    }

    @Override
    public long getCount(final Difficulte difficulte) {
        return shardedMap.get(checkNotNull(difficulte)).getCount();
    }

    @Override
    public long generateNewId(final Difficulte difficulte) {
        ShardedCounter compteur = shardedMap.get(checkNotNull(difficulte));
        long newId;

        synchronized (compteur) {
            compteur.increment();
            newId = compteur.getCount();
        }

        long finalId = Long.valueOf(difficulte.getImportance() + String.valueOf(newId));
        addIdToRandomBo(difficulte, finalId);

        return finalId;
    }

    private void addIdToRandomBo(final Difficulte difficulte, final long id) {
        ofy().transactNew(new VoidWork() {

            @Override
            public void vrun() {

                // RandomBo randomBo = ofy().load().type(RandomBo.class).id(difficulte.name()).now();

                RandomBo randomBo = ofy().load().entity(new RandomBo(difficulte)).now();

                if (randomBo == null) {
                    randomBo = new RandomBo();
                    randomBo.setDifficulte(difficulte);
                }
                randomBo.getIdListe().add(id);
                ofy().save().entity(randomBo).now();
            }
        });
    }
}
