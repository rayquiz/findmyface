package fr.rayquiz.findmyface.utils;

import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Query;

public class ObjectifyUtil {
    /**
     * Méthode simulant une méthode startsWith avec Objectify
     * 
     * @param query
     * @param field
     * @param search
     * @return
     */
    public static <T> Query<T> fieldStartsWith(Query<T> query, final String field, final String search) {
        query = query.filter(field + " >=", search);
        return query.filter(field + " <=", search + "\ufffd");
    }

    /**
     * Méthode simulant une méthode startsWith avec Objectify
     * 
     * @param query
     * @param field
     * @param search
     * @return
     */
    public static <T> Query<T> fieldStartsWith(final LoadType<T> query, final String field, final String search) {
        Query<T> retour = query.filter(field + " >=", search);
        return retour.filter(field + " <=", search + "\ufffd");
    }
}
