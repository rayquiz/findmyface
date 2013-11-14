package fr.rayquiz.findmyface.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.googlecode.objectify.ObjectifyService;

import fr.rayquiz.findmyface.bo.Indice;
import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.dao.IPersonneDao;

public class PersonneDaoTest {

    private final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig(),
            new LocalMemcacheServiceTestConfig());

    private final IPersonneDao service = new PersonneDao();

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    @Test
    public void should_save_entity_immediately() {
        // Arrange
        Personne p = new Personne();
        p.setNom("Marcel");
        p.setPrenom("Rene");
        p.setAnneeDeces(2006);
        p.setAnneeNaissance(1950);

        // Act
        long idRetour = service.saveImmediate(p);

        // Assert
        assertThat(p.getId()).isNotNull().isNotEqualTo(0).isEqualTo(idRetour);
        assertThat(ObjectifyService.ofy().load().type(Personne.class).count()).isEqualTo(1);
    }

    @Test
    public void should_save_entity_with_indices_immediately() {
        // Arrange
        Personne p = new Personne();
        p.setNom("Nom");
        p.setPrenom("Prenom");
        p.getIndiceListe().add(new Indice(1, "enonce 1"));

        // Act
        long idRetour = service.saveImmediate(p);
        long nbEnregistrement = ObjectifyService.ofy().load().type(Personne.class).count();

        // Assert
        assertThat(p.getId()).isNotNull().isNotEqualTo(0).isEqualTo(idRetour);
        assertThat(nbEnregistrement).isEqualTo(1);
    }

    @Test
    public void should_save_entity_asynchronously() {
        // Arrange
        Personne p = new Personne();
        p.setNom("Nom");
        p.setPrenom("Prenom");

        // Act
        service.saveAsynchronously(p);

        // Assert
        assertThat(p.getId()).isNull();
    }

    @Test
    public void should_load_entity() {
        // Arrange
        Personne retour = null;
        Personne p = new Personne();
        p.setNom("Mon prénom");
        p.setPrenom("Mon nom");

        // Act
        long idRetour = service.saveImmediate(p);
        retour = service.getById(idRetour);

        // Assert
        assertThat(retour).isEqualTo(p);
    }

    @Test
    public void should_load_entity_with_indices() {
        // Arrange
        Personne retour = null;
        Personne p = new Personne();
        p.setNom("Mon prénom");
        p.setPrenom("Mon nom");
        p.getIndiceListe().add(new Indice(1, "enonce 1"));
        p.getIndiceListe().add(new Indice(2, "enonce 2"));

        // Act
        long idRetour = service.saveImmediate(p);
        retour = service.getById(idRetour);

        // Assert
        assertThat(retour).isEqualTo(p);
        assertThat(p.getIndiceListe()).isSameAs(retour.getIndiceListe());
    }
}
