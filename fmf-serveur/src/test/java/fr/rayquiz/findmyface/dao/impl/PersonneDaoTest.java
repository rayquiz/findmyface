package fr.rayquiz.findmyface.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalMemcacheServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Lists;
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

    @Test
    public void should_find_personne_by_nom() {
        // Arrange
        List<Personne> liste = generatePersonnes(10);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("EinsteinBerger").withPrenom("Jean-Rene");

        // Act
        saveAll(liste);
        Set<Personne> retour = service.getByNomOuPrenomPhonetic("Einst");

        // Assert
        assertThat(retour).containsOnly(liste.get(5), liste.get(8));
    }

    @Test
    public void should_find_personne_by_prenom() {
        // Arrange
        List<Personne> liste = generatePersonnes(10);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("EinsteinBerger").withPrenom("Jean-Rene");
        liste.get(1).withNom("Michel").withPrenom("Jean-Pierre");

        // Act
        saveAll(liste);
        Set<Personne> retour = service.getByNomOuPrenomPhonetic("jean");

        // Assert
        assertThat(retour).containsOnly(liste.get(8), liste.get(1));
    }

    @Test
    public void should_find_personne_whith_same_prenom_and_nom_without_double() {
        // Arrange
        List<Personne> liste = generatePersonnes(10);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("EinsteinBerger").withPrenom("Jean-Rene");
        liste.get(2).withNom("Michel").withPrenom("Michel");

        // Act
        saveAll(liste);
        Set<Personne> retour = service.getByNomOuPrenomPhonetic("michel");

        // Assert
        assertThat(retour).hasSize(1).containsOnly(liste.get(2));
    }

    @Test
    public void should_find_personne_by_prenom_and_nom() {
        // Arrange
        List<Personne> liste = generatePersonnes(20);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("Miste").withPrenom("Pierre-Mathieu");
        liste.get(2).withNom("Chedid").withPrenom("Mathieu");
        liste.get(7).withNom("Mathieu").withPrenom("Mireille");

        // Act
        saveAll(liste);
        Set<Personne> retour = service.getByNomOuPrenomPhonetic("matthieu");

        // Assert
        assertThat(retour).hasSize(2).containsOnly(liste.get(2), liste.get(7));
    }

    private void saveAll(final List<Personne> liste) {
        for (Personne personne : liste) {
            service.saveImmediate(personne);
        }
    }

    private List<Personne> generatePersonnes(final int nombre) {
        List<Personne> liste = Lists.newArrayList();
        for (int i = 0; i < nombre; i++) {
            Personne p = new Personne();
            p.setNom("Random" + i + RandomStringUtils.randomAlphabetic(20));
            p.setPrenom("Random" + i + RandomStringUtils.randomAlphabetic(20));
            liste.add(p);
        }
        return liste;
    }

}
